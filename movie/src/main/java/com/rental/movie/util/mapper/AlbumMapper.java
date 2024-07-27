package com.rental.movie.util.mapper;

import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.AlbumRequestDTO;
import com.rental.movie.model.dto.AlbumResponseDTO;
import com.rental.movie.model.entity.Album;
import com.rental.movie.repository.FilmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AlbumMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FilmRepository filmRepository;

    public AlbumResponseDTO convertToDTO(Album album) {
        return modelMapper.map(album, AlbumResponseDTO.class);
    }

    public Page<AlbumResponseDTO> convertToDTO(Page<Album> albums) {
        return albums.map(this::convertToDTO);
    }

    public Album convertToEntity(AlbumRequestDTO albumDTO) {
        Album album = modelMapper.map(albumDTO, Album.class);
        // Map films
        album.setFilm(albumDTO.getFilmId().stream().map(id -> {
            return filmRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        return album;
    }

    // Overloading for update album
    public Album convertToEntity(AlbumRequestDTO albumDTO, Album album) {
        modelMapper.map(albumDTO, album);
        // Map films
        album.setFilm(albumDTO.getFilmId().stream().map(id -> {
            return filmRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        return album;
    }
}
