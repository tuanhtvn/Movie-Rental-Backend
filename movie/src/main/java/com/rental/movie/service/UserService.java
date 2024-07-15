package com.rental.movie.service;

import java.util.Optional;

import com.rental.movie.model.entity.User;

public interface UserService {

    public void save(User user);

    public Optional<User> getByEmail(String email);

}
