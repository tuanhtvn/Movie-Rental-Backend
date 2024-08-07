package com.rental.movie.util.mapper;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.model.dto.SupportRequestResponseDTO;
import com.rental.movie.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rental.movie.model.dto.SupportRequestDTO;
import com.rental.movie.model.entity.SupportRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SupportRequestMapper {
    @Autowired
    private ModelMapper modelMapper;

    public SupportRequestDTO toDTO(SupportRequest supportRequest) {
        SupportRequestDTO supportRequestDTO = modelMapper.map(supportRequest, SupportRequestDTO.class);
        if(supportRequest.getUser() != null) {
            supportRequestDTO.setUserId(supportRequest.getUser().getId());
        }
        return supportRequestDTO;
    }

    public SupportRequest toEntity(SupportRequestDTO supportRequestDTO) {
        SupportRequest supportRequest = modelMapper.map(supportRequestDTO, SupportRequest.class);
        if(supportRequestDTO.getUserId() != null) {
            User user = new User();
            user.setId(supportRequestDTO.getUserId());
            supportRequest.setUser(user);
        }
        return supportRequest;
    }

    public SupportRequestResponseDTO toResponseDTO(SupportRequest supportRequest) {
        SupportRequestResponseDTO supportRequestResponseDTO = modelMapper.map(supportRequest, SupportRequestResponseDTO.class);
        if(supportRequest.getUser() != null) {
            supportRequestResponseDTO.setUserId(supportRequest.getUser().getId());
        }
        return supportRequestResponseDTO;
    }

    public List<SupportRequestResponseDTO> supportRequestResponseDTOSList(List<SupportRequest> supportRequests) {
        return supportRequests.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}