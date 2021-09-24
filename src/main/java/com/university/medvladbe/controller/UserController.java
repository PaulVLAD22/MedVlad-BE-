package com.university.medvladbe.controller;

import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.exception.BadLogin;
import com.university.medvladbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")

public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String usernameOrEmail,
                                          @RequestParam String password){
        try {
            return new ResponseEntity<>(userService.login(usernameOrEmail, password), HttpStatus.ACCEPTED);
        }
        catch (BadLogin e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}