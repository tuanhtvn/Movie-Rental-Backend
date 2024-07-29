package com.rental.movie.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.entity.Film;

public interface FilmService {
    public Film getById(String id);
    public List<FilmResponseDTO> getAllActivedFilm();       //isActive=true && isDeleted=false
    public List<FilmResponseDTO> getAllNotDeletedFilm();    //isDeleted=false
    public List<FilmResponseDTO> getAllDeletedFilm();       //isDeleted=true
    public Page<FilmResponseDTO> searchFilmByName(String keywords);
    public FilmResponseDTO createFilm(FilmResponseDTO filmDTO);
    public FilmResponseDTO  deleteFilmById(String filmId);
    public FilmResponseDTO updateFilmById(String filmId, FilmResponseDTO filmDTO);
    public FilmResponseDTO  changeStatusFilm(String filmId);
    public FilmResponseDTO  restoreFilmById(String filmId);

}
