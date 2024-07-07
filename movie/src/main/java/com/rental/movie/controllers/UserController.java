package com.rental.movie.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class UserController {
    @GetMapping("v1/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello");
    }
}
