package com.rental.movie.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.common.IAuthentication;
import com.rental.movie.model.dto.UserInfoResponseDTO;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Lazy
    @Autowired
    private IAuthentication authManager;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Optional<User> getByEmailAndAuthProvider(String email, AuthProvider authProvider) {
        return userRepository.findByEmailAndAuthProvider(email, authProvider);
    }

    @Override
    public Optional<User> getById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserInfoResponseDTO getUserInfo() {
        User user = authManager.getUserAuthentication();
        return modelMapper.map(user, UserInfoResponseDTO.class);
    }
}
