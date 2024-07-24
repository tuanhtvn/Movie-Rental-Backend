package com.rental.movie.service;

import java.time.Instant;
import java.util.List;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.ProfileRequestDTO;
import com.rental.movie.model.dto.ProfileResponseDTO;
import com.rental.movie.model.entity.Profile;
import com.rental.movie.model.entity.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserService userService;
    @Autowired
    private IAuthentication authManager;
    @Autowired
    private ModelMapper modelMapper;
    private Integer limitProfile = 10;

    @Override
    public void create(ProfileRequestDTO profileRequestDTO) {
        User user = authManager.getUserAuthentication();
        log.info("Creating new profile for user id: {}", user.getId());
        Profile profile = modelMapper.map(profileRequestDTO, Profile.class);
        profile.setId(new ObjectId().toString());
        // check if user has default profile
        profile.setIsDefault(user.getProfiles().isEmpty() ? true : false);
        // check limit profile
        if (user.getProfiles().size() >= limitProfile) {
            log.error("User: {} has reached the limit of profile", user.getId());
            throw new CustomException("Bạn chỉ được tạo tối đa " + limitProfile + " hồ sơ",
                    HttpStatus.BAD_REQUEST.value());
        }
        user.getProfiles().add(profile);
        userService.save(user);
        log.info("Create new profile for user id: {} successfully", user.getId());
    }

    @Override
    public List<ProfileResponseDTO> getAll() {
        User user = authManager.getUserAuthentication();
        log.info("Get all profiles for user id: {}", user.getId());
        return user.getProfiles().stream().map(profile -> {
            ProfileResponseDTO profileResponseDTO = modelMapper.map(profile, ProfileResponseDTO.class);
            profileResponseDTO.setIsChild(null);
            return profileResponseDTO;
        }).toList();
    }

    @Override
    public ProfileResponseDTO get(String id) {
        User user = authManager.getUserAuthentication();
        log.info("Get profile id: {} for user id: {}", id, user.getId());
        Profile profile = getById(id, user);
        return modelMapper.map(profile, ProfileResponseDTO.class);
    }

    @Override
    public void update(String id, ProfileRequestDTO profileRequestDTO) {
        User user = authManager.getUserAuthentication();
        log.info("Updating profile id: {} for user id: {}", id, user.getId());
        Profile profile = getById(id, user);
        modelMapper.map(profileRequestDTO, profile);
        profile.setUpdatedAt(Instant.now());
        userService.save(user);
        log.info("Update profile id: {} for user id: {} successfully", id, user.getId());
    }

    @Override
    public void delete(String id) {
        User user = authManager.getUserAuthentication();
        log.info("Deleting profile id: {} for user id: {}", id, user.getId());
        Profile profile = getById(id, user);
        // check if profile is default
        if (profile.getIsDefault()) {
            log.error("Cannot delete default profile");
            throw new CustomException("Không thể xóa hồ sơ mặc định", HttpStatus.BAD_REQUEST.value());
        }
        user.getProfiles().remove(profile);
        userService.save(user);
        log.info("Delete profile id: {} for user id: {} successfully", id, user.getId());
    }

    private Profile getById(String id, User user) {
        return user.getProfiles().stream().filter(p -> p.getId().equals(id)).findFirst().orElseThrow(() -> {
            log.error("Profile: {} not found", id);
            throw new CustomException("Không tìm thấy hồ sơ", HttpStatus.NOT_FOUND.value());
        });
    }
}
