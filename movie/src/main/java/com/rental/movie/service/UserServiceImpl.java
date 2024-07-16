package com.rental.movie.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getByEmailAndAuthProvider(String email, AuthProvider authProvider) {
        return userRepository.findByEmailAndAuthProvider(email, authProvider);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
