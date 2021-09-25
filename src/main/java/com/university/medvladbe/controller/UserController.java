package com.university.medvladbe.controller;

import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.exception.BadLogin;
import com.university.medvladbe.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
}