package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.UserCreationDTO;
import com.rental.movie.model.dto.UserInfoResponseDTO;
import com.rental.movie.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UserCMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserCreationDTO toDTO(User user) {
        return modelMapper.map(user, UserCreationDTO.class);
    }

    public User toEntity(UserCreationDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
