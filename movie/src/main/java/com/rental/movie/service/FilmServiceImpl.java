package com.rental.movie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.entity.Film;
import com.rental.movie.repository.FilmRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    @Autowired
    private FilmRepository filmRepository;

    @Override
    public Film getById(String id) {
        log.info("Get film by id: {}", id);
        return filmRepository.findById(id).orElseThrow(() -> {
            log.error("Film not found with id: {}", id);
            throw new CustomException("Không tìm thấy film", HttpStatus.NOT_FOUND.value());
        });
    }

    @Override
    public List<FilmResponseDTO> getAllActivedFilm() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllActivedFilm'");
    }

    @Override
    public List<FilmResponseDTO> getAllNotDeletedFilm() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllNotDeletedFilm'");
    }

    @Override
    public List<FilmResponseDTO> getAllDeletedFilm() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllDeletedFilm'");
    }

    @Override
    public Page<FilmResponseDTO> searchFilmByName(String keywords) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchFilmByName'");
    }

    @Override
    public FilmResponseDTO createFilm(FilmResponseDTO filmDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createFilm'");
    }

    @Override
    public void deleteFilmById(String filmId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFilmById'");
    }

    @Override
    public FilmResponseDTO updateFilmById(String filmId, FilmResponseDTO filmDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateFilmById'");
    }

    @Override
    public void changeStatusFilm(String filmId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeStatusFilm'");
    }

    @Override
    public void restoreFilmById(String filmId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'restoreFilmById'");
    }

}
