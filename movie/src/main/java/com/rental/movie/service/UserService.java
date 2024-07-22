package com.rental.movie.service;

import java.util.Optional;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.model.dto.ChangePassRequestDTO;
import com.rental.movie.model.dto.UserInfoRequestDTO;
import com.rental.movie.model.dto.UserInfoResponseDTO;
import com.rental.movie.model.entity.User;

public interface UserService {

    public User save(User user);

    public Optional<User> getByEmailAndAuthProvider(String email, AuthProvider authProvider);

    public Optional<User> getById(String id);

    public UserInfoResponseDTO getUserInfo();

    public UserInfoResponseDTO updateUserInfo(UserInfoRequestDTO userInfoRequestDTO);

    public void changePassword(ChangePassRequestDTO changePassRequestDTO);
}
