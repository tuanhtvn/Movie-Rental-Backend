package com.rental.movie.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.entity.User;
import com.rental.movie.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthManager implements IAuthentication {

    @Autowired
    private UserService userService;

    @Override
    public User getUserAuthentication() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getById(id).orElseThrow(() -> {
            log.error("User: {} not found", id);
            throw new CustomException("Không tìm thấy người dùng", HttpStatus.NOT_FOUND.value());
        });
    }

}
