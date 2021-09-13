package com.university.medvladbe.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/test")
public class TestController {

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/sample", method = RequestMethod.GET, produces = "application/json")
    @GetMapping("/cox")
    public String getCox(){
        return "COX";
    }
}
