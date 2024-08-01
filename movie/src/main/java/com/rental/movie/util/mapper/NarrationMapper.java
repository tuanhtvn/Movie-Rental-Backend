package com.rental.movie.util.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.rental.movie.model.dto.NarrationRequestDTO;
import com.rental.movie.model.dto.NarrationResponseDTO;
import com.rental.movie.repository.NarrationRepository;
import com.rental.movie.model.entity.Narration;

@Component
public class NarrationMapper {

    @Autowired
    private ModelMapper modelMapper;

    public NarrationResponseDTO convertToDTO(Narration narration) {
        return modelMapper.map(narration, NarrationResponseDTO.class);
    }

    public Narration convertToEntity(NarrationRequestDTO narrationDTO) {
        return modelMapper.map(narrationDTO, Narration.class);
    }

    public Narration convertToEntity(NarrationRequestDTO dto, Narration narration) {
        modelMapper.map(dto, narration);
        return narration;
    }
}
