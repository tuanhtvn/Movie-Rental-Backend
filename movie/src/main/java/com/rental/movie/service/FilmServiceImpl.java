package com.rental.movie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private FilmMapper filmMapper;

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
        Page<Film> films = filmRepository.findAllByActived(pageable, search);
        log.info("Get all actived films: {}", films.getContent().toString());
        if (films.isEmpty()) {
            log.error("No films found");
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return filmMapper.convertToDTO(films);
    }

    @Override
    public List<FilmResponseDTO> getAllNotDeletedFilm() {
        Page<Film> films = filmRepository.findAllByNotDeleted(pageable, search);
        log.info("Get all not deleted films: {}", films.getContent().toString());
        if (films.isEmpty()) {
            log.error("No films found");
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return filmMapper.convertToDTO(films);
    }

    @Override
    public List<FilmResponseDTO> getAllDeletedFilm() {
        Page<Film> films = filmRepository.findAllByDeleted(pageable, search);
        log.info("Get all deleted films: {}", films.getContent().toString());
        if (films.isEmpty()) {
            log.error("No films found");
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return filmMapper.convertToDTO(films);
    }

    @Override
    public Page<FilmResponseDTO> searchFilmByName(String keywords) {
        List<Film> films = filmRepository.findDeletedFilmsByTitleContainingIgnoreCase(title);
        if (films.isEmpty()) {
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return films.stream().map(filmMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public FilmResponseDTO createFilm(FilmResponseDTO filmDTO) {
        Film film = filmMapper.convertToEntity(filmDTO);

        film.setActive(false);
        film.setDeleted(false);

        log.info("Create film {}", film.toString());
        Film savedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(savedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO deleteFilmById(String filmId) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new CustomException("Phim với ID " + id + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        film.setDeleted(true);

        log.info("Soft delete film {}", film.toString());
        Film deletedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(deletedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO updateFilmById(String filmId, FilmResponseDTO filmDTO) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new CustomException("Phim với ID " + id + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        log.info("Update film {}", film.toString());
        Film updateFilm = filmMapper.convertToEntity(FilmDTO, film);
        return filmMapper.convertToDTO(filmRepository.save(updateFilm));
    }

    @Override
    @Transactional
    public FilmResponseDTO changeStatusFilm(String filmId) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new CustomException("Phim với ID " + id + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        film.setActive(!film.isActive());

        log.info("Change status film {}", film.toString());
        Film updatedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(updatedFilm);
    }

    @Override
    @Transactional
    public void restoreFilmById(String filmId) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new CustomException("Phim với ID " + id + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        film.setDeleted(false);

        log.info("Restore film {}", film.toString());
        Film restoredFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(restoredFilm);

    }

}
