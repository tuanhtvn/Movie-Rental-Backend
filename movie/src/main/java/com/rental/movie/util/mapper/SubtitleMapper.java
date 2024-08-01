package com.rental.movie.util.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.rental.movie.model.dto.SubtitleRequestDTO;
import com.rental.movie.model.dto.SubtitleResponseDTO;
import com.rental.movie.repository.SubtitleRepository;
import com.rental.movie.model.entity.Subtitle;

@Component
public class SubtitleMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SubtitleResponseDTO convertToDTO(Subtitle subtitle) {
        return modelMapper.map(subtitle, SubtitleResponseDTO.class);
    }

    public Subtitle convertToEntity(SubtitleRequestDTO subtitleDTO) {
        return modelMapper.map(subtitleDTO, Subtitle.class);
    }

    public Subtitle convertToEntity(SubtitleRequestDTO dto, Subtitle subtitle) {
        modelMapper.map(dto, subtitle);
        return subtitle;
    }
}
