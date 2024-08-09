package com.rental.movie.service;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.common.IAuthentication;
import com.rental.movie.common.Role;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.UserCreationDTO;
import com.rental.movie.model.dto.UserResponseDTO;
import com.rental.movie.model.entity.User;
import com.rental.movie.util.mapper.UserCMapper;
import com.rental.movie.repository.UserRepository;
import com.rental.movie.util.mapper.UserMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagerServiceImpl implements UserManagerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCMapper userCMapper;

    @Autowired
    private IAuthentication authentication;
    @Override
    public UserCreationDTO createUser(UserCreationDTO userDTO) {
        if (userDTO.getAuthProvider() != AuthProvider.LOCAL) {
            throw new CustomException("Chỉ có thể tạo tài khoản với phương thức đăng nhập LOCAL.",
                    HttpStatus.FORBIDDEN.value());
        }
        Optional<User> existingUser = userRepository.findByEmailAndAuthProvider(userDTO.getEmail(),AuthProvider.LOCAL);
        if (existingUser.isPresent()) {
            throw new CustomException("Email đăng ký đã tồn tại",
                    HttpStatus.FORBIDDEN.value());
        }
        if (userDTO.getRole() != Role.USER && userDTO.getRole() != Role.EMPLOYEE) {
            throw new CustomException("Chỉ có thể tạo tài khoản với quyền USER hoặc EMPLOYEE.",HttpStatus.FORBIDDEN.value());
        }
        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
        userDTO.setPassword(hashedPassword);
        User user = userCMapper.toEntity(userDTO);
        user.setPassword(hashedPassword);
        user.setAuthProvider(AuthProvider.LOCAL);
        return userCMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserCreationDTO updateUser(String id, UserCreationDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CustomException("Không tìm thấy người dùng",
                            HttpStatus.NOT_FOUND.value());
                });

        Optional<User> userWithEmail = userRepository.findByEmailAndAuthProvider(userDTO.getEmail(),AuthProvider.LOCAL);
        if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(id)) {
            throw new CustomException("Email đã tồn tại trong hệ thống.",HttpStatus.FORBIDDEN.value());
        }

        if (userDTO.getAuthProvider() != AuthProvider.LOCAL) {
            throw new CustomException("Chỉ có thể cập nhật tài khoản với phương thức đăng nhập LOCAL.",
                    HttpStatus.FORBIDDEN.value());
        }
        if (userDTO.getRole() != Role.USER && userDTO.getRole() != Role.EMPLOYEE) {
            throw new CustomException("Chỉ có thể cập nhật tài khoản với quyền USER hoặc EMPLOYEE.",HttpStatus.FORBIDDEN.value());
        }

        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
        userDTO.setPassword(hashedPassword);

        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setPassword(hashedPassword);
        existingUser.setAuthProvider(userDTO.getAuthProvider());
        existingUser.setRole(userDTO.getRole());
        existingUser = userRepository.save(existingUser);

        return userCMapper.toDTO(existingUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) {
            throw new CustomException("Không tìm thấy người dùng nào",
                    HttpStatus.NOT_FOUND.value());
        }
        return userMapper.toResponseDTOList(users);
    }

    @Override
    public void deleteUser(String id) {
        User userToDelete = userRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("Không tìm thấy người dùng",
                    HttpStatus.NOT_FOUND.value());
        });
        User currentAdmin = authentication.getUserAuthentication();
        if(userToDelete.getId().equals(currentAdmin.getId())) {
            throw new CustomException("Không thể xóa tài khoản hiện tại",
                    HttpStatus.FORBIDDEN.value());
        }
        if(userToDelete.getRole() == Role.ADMIN) {
            throw new CustomException("Không thể xóa tài khoản admin",
                    HttpStatus.FORBIDDEN.value());
        }
        userRepository.deleteById(id);
    }

    @Override
    public void activateUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("Không tìm thấy người dùng",
                    HttpStatus.NOT_FOUND.value());
        });
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("Không tìm thấy người dùng",
                    HttpStatus.NOT_FOUND.value());
        });
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public void softDeleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("Không tìm thấy người dùng",
                    HttpStatus.NOT_FOUND.value());
        });
        user.setIsDeleted(true);
        userRepository.save(user);
    }
}
