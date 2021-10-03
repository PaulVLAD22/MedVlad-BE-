package com.university.medvladbe.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/cox")
    public String getCox(){
        return "COX";
    }
}
