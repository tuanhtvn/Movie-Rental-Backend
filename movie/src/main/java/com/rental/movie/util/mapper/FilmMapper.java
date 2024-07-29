package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.model.entity.Film;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FilmMapper {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FilmRepository filmRepository;

    public FilmResponseDTO convertToDTO(Film film) {
        return modelMapper.map(film, FilmResponseDTO.class);
    }

    public Page<FilmResponseDTO> convertToDTO(Page<Film> films) {
        return films.map(this::convertToDTO);
    }

    public Film convertToEntity(FilmRequestDTO filmDTO) {
        Film film = modelMapper.map(filmDTO, Film.class);
        return film;
    }

    public Film convertToEntity(FilmRequestDTO dto, Film film) {
        modelMapper.map(dto, film);
        return film;
    }
}
