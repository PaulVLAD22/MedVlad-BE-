package com.university.medvladbe.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.medvladbe.entity.account.Role;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
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

    @Autowired
    public UserController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>>getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

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
    @PostMapping("/user/save")
    public ResponseEntity<User>saveUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.saveUser(user));
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
                        .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))// 10 minute
                        .withIssuer(request.getRequestURL().toString())
                        //punem rolul in refreshToken
                        .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token",accessToken);
                tokens.put("refresh_token",refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);

            } catch (Exception exception) {

                log.error("Error logging in: {}",exception.getMessage());
                response.setHeader("error",exception.getMessage());
                //response.sendError(403);//forbidden
                response.setStatus(403);
                Map<String, String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }

        }
        else{
            throw new RuntimeException("Refresh Token is missing");
        }

    }
}