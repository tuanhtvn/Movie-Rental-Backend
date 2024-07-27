package com.rental.movie.util.mapper;

import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.GenreRequestDTO;
import com.rental.movie.model.dto.GenreResponseDTO;
import com.rental.movie.model.entity.Genre;
import com.rental.movie.repository.FilmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GenreMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FilmRepository filmRepository;

    public GenreResponseDTO convertToDTO(Genre genre) {
        return modelMapper.map(genre, GenreResponseDTO.class);
    }

    public Page<GenreResponseDTO> convertToDTO(Page<Genre> genres) {
        return genres.map(this::convertToDTO);
    }

    public Genre convertToEntity(GenreRequestDTO genreDTO) {
        Genre genre = modelMapper.map(genreDTO, Genre.class);
        // Map Films
        genre.setFilm(genreDTO.getFilmsId().stream().map(id -> {
            return filmRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy film", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        return genre;
    }

    // Overloading for update category
    public Genre convertToEntity(GenreRequestDTO dto, Genre genre) {
        modelMapper.map(dto, genre);
        // Map Films
        genre.setFilm(dto.getFilmsId().stream().map(id -> {
            return filmRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy film", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        return genre;
    }
}
