package com.rental.movie.service;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.model.dto.UserCreationDTO;
import com.rental.movie.model.dto.UserInfoResponseDTO;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.UserManagerRepository;
import com.rental.movie.util.mapper.UserCMapper;
import com.rental.movie.util.mapper.UserManagerMapper;
import com.rental.movie.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
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

    @Autowired
    private UserCMapper userCMapper;

    @Autowired
    private UserManagerRepository userManagerRepository;

    @Override
    public UserCreationDTO createUser(UserCreationDTO userDTO) {
        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
        userDTO.setPassword(hashedPassword);
        User user = userCMapper.toEntity(userDTO);
        user.setPassword(hashedPassword);
        user.setAuthProvider(AuthProvider.LOCAL);
        return userCMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserCreationDTO updateUser(String id, UserCreationDTO userDTO) {
        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
        userDTO.setPassword(hashedPassword);
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setPassword(hashedPassword);
        existingUser.setAuthProvider(userDTO.getAuthProvider());
        existingUser.setRole(userDTO.getRole());
        existingUser = userRepository.save(existingUser);
        return userCMapper.toDTO(existingUser);
    }

    @Override
    public List<UserCreationDTO> getAllUsers() {
        return userManagerRepository.findAll().stream().map(userCMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
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
