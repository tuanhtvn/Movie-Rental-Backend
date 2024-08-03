package com.rental.movie.service;

import com.rental.movie.model.dto.GenreRequestDTO;
import com.rental.movie.model.dto.GenreResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenreService {
    public Page<GenreResponseDTO> getAllActiveGenres(Pageable pageable, String search); //isActive=true && isDeleted=false
    public Page<GenreResponseDTO> getAllInactiveGenres(Pageable pageable, String search); //isActive=false && isDeleted=false
    public Page<GenreResponseDTO> getAllSoftDeletedGenres(Pageable pageable, String search); //isDeleted=true
    public Page<GenreResponseDTO> getAll(Pageable pageable, String search);
    public GenreResponseDTO getGenreById(String genreId);
    public GenreResponseDTO createGenre(GenreRequestDTO genreDTO);
    public GenreResponseDTO updateGenre(String genreId, GenreRequestDTO genreDTO);
    public void softDeleteGenre(String genreId);
    public void restoreGenre(String genreId);
//    public void activateGenre(String genreId);
//    public void deactivateGenre(String genreId);
    public boolean toggleActiveStatus(String genreId);
}
