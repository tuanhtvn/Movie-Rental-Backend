package com.rental.movie.service;

import java.util.Optional;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.model.entity.User;

public interface UserService {

    public User save(User user);

    public Optional<User> getByEmailAndAuthProvider(String email, AuthProvider authProvider);

    public Optional<User> getById(String id);
}
