package com.rental.movie.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rental.movie.model.Comment;
import com.rental.movie.model.dto.CommentRequestDTO;
import com.rental.movie.model.dto.CommentResponseDTO;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.entity.Film;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.util.mapper.FilmMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private FilmMapper filmMapper;

    @Override
    public Film getById(String id) {
        //log.info("Get film by id: {}", id);
        return filmRepository.findById(id).orElseThrow(() -> {
            //log.error("Film not found with id: {}", id);
            throw new CustomException("Không tìm thấy film", HttpStatus.NOT_FOUND.value());
        });
    }

    @Override
    public Page<FilmResponseDTO> getAllActivedFilm(Pageable pageable, String search) {
        Page<Film> films = filmRepository.findAllByActived(pageable, search);
        //log.info("Get all actived films: {}", films.getContent().toString());
        if (films.isEmpty()) {
            log.error("No films found");
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return filmMapper.convertToDTO(films);
    }

    @Override
    public Page<FilmResponseDTO> getAllNotDeletedFilm(Pageable pageable, String search) {
        Page<Film> films = filmRepository.findAllByNotDeleted(pageable, search);
        //log.info("Get all not deleted films: {}", films.getContent().toString());
        if (films.isEmpty()) {
            log.error("No films found");
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return filmMapper.convertToDTO(films);
    }

    @Override
    public Page<FilmResponseDTO> getAllDeletedFilm(Pageable pageable, String search) {
        Page<Film> films = filmRepository.findAllByDeleted(pageable, search);
        //log.info("Get all deleted films: {}", films.getContent().toString());
        if (films.isEmpty()) {
            log.error("No films found");
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return filmMapper.convertToDTO(films);
    }

    @Override
    public List<Film> searchFilmByName(String keywords) {
        List<Film> films = filmRepository.findByKeywords(keywords);
        if (films.isEmpty()) {
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return films;
    }

    @Override
    public FilmResponseDTO createFilm(FilmRequestDTO filmDTO) {
        Film film = filmMapper.convertToEntity(filmDTO);

        //log.info("Create film {}", film.toString());
        Film savedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(savedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO deleteFilmById(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        film.setisDeleted(true);

        //log.info("Soft delete film {}", film.toString());
        Film deletedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(deletedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO updateFilmById(String filmId, FilmRequestDTO filmDTO) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        //log.info("Update film {}", film.toString());
        Film updateFilm = filmMapper.convertToEntity(filmDTO, film);
        return filmMapper.convertToDTO(filmRepository.save(updateFilm));
    }

    @Override
    @Transactional
    public FilmResponseDTO activeFilm(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        film.setisActive(true);

        //log.info("Active film {}", film.toString());
        Film updatedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(updatedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO deactiveFilm(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        film.setisActive(false);

        //log.info("Deactive film {}", film.toString());
        Film updatedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(updatedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO restoreFilmById(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        film.setisDeleted(false);

        //log.info("Restore film {}", film.toString());
        Film restoredFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(restoredFilm);

    }

}
