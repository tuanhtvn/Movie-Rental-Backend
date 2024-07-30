package com.rental.movie.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.entity.Film;

public interface FilmService {
    public Film getById(String id);
    public Page<FilmResponseDTO> getAllActivedFilm(Pageable pageable, String search);       //isActive=true && isDeleted=false
    public Page<FilmResponseDTO> getAllNotDeletedFilm(Pageable pageable, String search);    //isDeleted=false
    public Page<FilmResponseDTO> getAllDeletedFilm(Pageable pageable, String search);       //isDeleted=true
    public List<Film> searchFilmByName(String keywords);
    public FilmResponseDTO createFilm(FilmRequestDTO filmDTO);
    public FilmResponseDTO deleteFilmById(String filmId);
    public FilmResponseDTO updateFilmById(String filmId, FilmRequestDTO filmDTO);
    public FilmResponseDTO activeFilm(String filmId);
    public FilmResponseDTO deactiveFilm(String filmId);
    public FilmResponseDTO restoreFilmById(String filmId);
}
