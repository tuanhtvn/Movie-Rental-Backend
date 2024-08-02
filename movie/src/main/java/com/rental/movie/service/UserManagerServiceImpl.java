package com.rental.movie.service;

import com.rental.movie.model.dto.UserInfoResponseDTO;
import com.rental.movie.model.entity.User;
import com.rental.movie.util.mapper.UserManagerMapper;
import com.rental.movie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagerServiceImpl implements UserManagerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserManagerMapper userMapper;

    @Override
    public UserInfoResponseDTO createUser(UserInfoResponseDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public UserInfoResponseDTO updateUser(String id, UserInfoResponseDTO userDTO) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFullName(userDTO.getFullName());
        existingUser = userRepository.save(existingUser);
        return userMapper.toDTO(existingUser);
    }

    @Override
    public UserInfoResponseDTO getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserInfoResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public void activateUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public void softDeleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        user.setIsDeleted(true);
        userRepository.save(user);
    }
}
