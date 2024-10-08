package com.rental.movie.service;

import com.rental.movie.model.dto.UserCreationDTO;
import com.rental.movie.model.dto.UserResponseDTO;

import java.util.List;

public interface UserManagerService {
    UserCreationDTO createUser(UserCreationDTO userDTO);
    UserCreationDTO updateUser(String id, UserCreationDTO userDTO);
    List<UserResponseDTO> getAllUsers();
    void  softDeleteUser(String id);
    void deleteUser(String id);
    void activateUser(String id);
    void deactivateUser(String id);
}
