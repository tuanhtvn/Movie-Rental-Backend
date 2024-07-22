package com.rental.movie.service;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.common.IAuthentication;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.ChangePassRequestDTO;
import com.rental.movie.model.dto.UserInfoRequestDTO;
import com.rental.movie.model.dto.UserInfoResponseDTO;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

    @Override
    public UserInfoResponseDTO updateUserInfo(UserInfoRequestDTO userInfoRequestDTO) {
        User user = authManager.getUserAuthentication();
        user.setFullName(userInfoRequestDTO.getFullName());
        userRepository.save(user);
        return modelMapper.map(user, UserInfoResponseDTO.class);
    }

    @Override
    public void changePassword(ChangePassRequestDTO changePassRequestDTO) {
        User user = authManager.getUserAuthentication();
        log.info("Change password for user: {}", user.getEmail());
        if (!BCrypt.checkpw(changePassRequestDTO.getPasswordOld(), user.getPassword())) {
            log.error("Old password is not correct");
            throw new CustomException("Mật khẩu cũ không đúng", HttpStatus.UNAUTHORIZED.value());
        }
        if (!changePassRequestDTO.getPasswordNew().equals(changePassRequestDTO.getPasswordConfirm())) {
            log.error("New password and confirm password are not the same");
            throw new CustomException("Mật khẩu mới và xác nhận mật khẩu mới không khớp",
                    HttpStatus.BAD_REQUEST.value());
        }
        user.setPassword(BCrypt.hashpw(changePassRequestDTO.getPasswordNew(), BCrypt.gensalt()));
        userRepository.save(user);
    }
}
