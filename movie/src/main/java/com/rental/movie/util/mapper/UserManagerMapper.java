package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.UserInfoResponseDTO;
import com.rental.movie.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserManagerMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UserManagerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserInfoResponseDTO toDTO(User user) {
        return modelMapper.map(user, UserInfoResponseDTO.class);
    }

    public User toEntity(UserInfoResponseDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
