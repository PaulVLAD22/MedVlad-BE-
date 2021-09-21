package com.university.medvladbe.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/cox")
    public String getCox(){
        return "COX";
    }
}
