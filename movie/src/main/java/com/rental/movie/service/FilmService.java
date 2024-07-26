package com.rental.movie.service;

import com.rental.movie.model.entity.Film;

public interface FilmService {
    public Film getById(String id);
    public List<FilmResponseDTO> getAllActivedFilm();       //isActive=true && isDeleted=false
    public List<FilmResponseDTO> getAllNotDeletedFilm();    //isDeleted=false
    public List<FilmResponseDTO> getAllDeletedFilm();       //isDeleted=true
    public Page<FilmResponseDTO> searchFilmByName(String keywords);
    public FilmResponseDTO createFilm(FilmResponseDTO filmDTO);
    public void deleteFilmById(String filmId);
    public FilmResponseDTO updateFilmById(String filmId, FilmResponseDTO filmDTO);
    public void changeStatusFilm(String filmId);
    public void restoreFilmById(String filmId);

}
