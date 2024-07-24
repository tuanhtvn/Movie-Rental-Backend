package com.rental.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rental.movie.exception.CustomException;
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

}
