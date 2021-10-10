package com.university.medvladbe.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.entity.account.Role;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.service.QuestionService;
import com.university.medvladbe.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
public class UserController {

    private UserDetailsServiceImpl userService;
    private QuestionService questionService;

    @Autowired
    public UserController(UserDetailsServiceImpl userService,
                          QuestionService questionService) {
        this.userService = userService;
        this.questionService = questionService;
    }

//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getUsers() {
//        return ResponseEntity.ok().body(userService.getUsers());
//    }

    //    @GetMapping("/login")
//    public ResponseEntity<User> loginUser(@RequestParam String usernameOrEmail,
//                                          @RequestParam String password){
//        try {
//            return new ResponseEntity<>(userService.login(usernameOrEmail, password), HttpStatus.ACCEPTED);
//        }
//        catch (BadLogin e){
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//    }
//    @PostMapping("/user/save")
//    public ResponseEntity<User> saveUser(@RequestBody User user) {
//        return ResponseEntity.ok().body(userService.saveUser(user));
//    }

    @PostMapping("user/postQuestion")
    public ResponseEntity postQuestion(@RequestParam String content) {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        log.info(userName);

        try {
            questionService.postQuestion(userName, content);
            return new ResponseEntity(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getQuestions")
    public List<QuestionDto> getQuestions() {
        return questionService.getQuestions();
    }

    @GetMapping("token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());// secret seteaza-l undeva ascuns
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);

                String accessToken = JWT.create()
                        //aici punem informatiile refreshToken-ului
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))// 10 minute
                        .withIssuer(request.getRequestURL().toString())
                        //punem rolul in refreshToken
                        .withClaim("role", user.getRole().getName().toString())
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception exception) {

                log.error("Error logging in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                //response.sendError(403);//forbidden
                response.setStatus(403);
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }

        } else {
            throw new RuntimeException("Refresh Token is missing");
        }

    }
}