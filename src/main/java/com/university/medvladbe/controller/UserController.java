package com.university.medvladbe.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.university.medvladbe.dto.AdminHistoryDto;
import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.dto.UserDto;
import com.university.medvladbe.entity.account.Role;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.registration.RegistrationResult;
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
import javax.xml.ws.Response;
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
    public UserController(UserDetailsServiceImpl userService
    ) {
        this.userService = userService;
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
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))// 30 min
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

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestParam String email,
                                       @RequestParam String username,
                                       @RequestParam String password,
                                       @RequestParam String licensePicture,
                                       @RequestParam String role) {
        log.info(email + username + password + licensePicture + role);

        userService.registerUser(email, username, password, role, licensePicture);
        return new ResponseEntity(HttpStatus.OK);
        //TODO:: ii tirmiti link care face cererea asta prin email dupa ce apasa pe register
    }

    @GetMapping("/getUserByUsername")
    public UserDto getUserByUsername(@RequestParam String username) {
        User user = userService.getUser(username);
        return user.userDtoFromUser();
    }

    @GetMapping("/admin/getAdminHistory")
    public AdminHistoryDto getAdminHistory() {
        String adminUsername = getCurrentUsername();
        return userService.getAdminHistory(adminUsername);
    }

    @GetMapping("/admin/getInactiveUsers")
    public List<UserDto> getInactiveUsers() {
        return userService.getInactiveUsers();
    }


    @GetMapping("/admin/getLastInactiveDoctor")
    public UserDto getLastInactiveDoctor() {
        return userService.getInactiveDoctors();
    }

    @PostMapping("/admin/acceptUserRegistration")
    public void acceptUserRegistration(@RequestParam String username,
                                       @RequestParam String comment,
                                       @RequestParam boolean verdict) {
        String adminUsername = getCurrentUsername();
        System.out.println(username + " " + comment + " " + verdict);
        userService.acceptUserRegistration(adminUsername, username, comment, verdict);
    }

    @PutMapping("/updateFirstName")
    public void updateFirstName(@RequestParam String firstName) {
        userService.updateFirstName(getCurrentUsername(), firstName);
    }

    @PutMapping("/updateLastName")
    public void updateLastName(@RequestParam String lastName) {
        userService.updateLastName(getCurrentUsername(), lastName);
    }

    @PutMapping("/updateProfilePicture")
    public void updateProfilePicture(@RequestParam String profilePicture) {
        userService.updateProfilePicture(getCurrentUsername(), profilePicture);
    }


    private String getCurrentUsername() {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
