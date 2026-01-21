package com.example.hellospring;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HelloController {
    @GetMapping("/")
    public String hello() {
        return "Hello from Spring Boot 4.0.1. + Java 25!";
    }
        
}
