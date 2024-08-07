package com.rental.movie.service;

import com.rental.movie.model.dto.UserCreationDTO;
import com.rental.movie.model.dto.UserInfoResponseDTO;

import java.util.List;

public interface UserManagerService {
    UserCreationDTO createUser(UserCreationDTO userDTO);
    UserInfoResponseDTO updateUser(String id, UserInfoResponseDTO userDTO);
    UserInfoResponseDTO getUserById(String id);
    List<UserInfoResponseDTO> getAllUsers();
    void  softDeleteUser(String id);
    void deleteUser(String id);
    void activateUser(String id);
    void deactivateUser(String id);
}
