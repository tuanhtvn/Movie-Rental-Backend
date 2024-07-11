package com.rental.movie.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.movie.common.BaseResponse;

@RestController
@RequestMapping("api/")
public class UserController {
    @GetMapping("v1/hello")
    public ResponseEntity<BaseResponse> hello() {
        BaseResponse response = BaseResponse.builder().status(HttpStatus.OK.value()).message("hello").data(null)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
