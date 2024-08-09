package com.rental.movie.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.OptionalDouble;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.entity.Film;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.util.mapper.FilmMapper;
import com.rental.movie.model.dto.RatingRequestDTO;
import com.rental.movie.repository.GenreRepository;
import com.rental.movie.repository.SubtitleRepository;
import com.rental.movie.repository.NarrationRepository;
import com.rental.movie.repository.CommentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private SubtitleRepository subtitleRepository;
    @Autowired
    private NarrationRepository narrationRepository;
    @Autowired
    private CommentRepository commentRepository;
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
        String keyword = removeAccents(keywords).toLowerCase();

        return filmRepository.findByActived().stream()
                .filter(film -> {
                    String films = removeAccents(film.getFilmName()).toLowerCase();
                    return films.contains(keyword);
                })
                .collect(Collectors.toList());
    }

    public static String removeAccents(String text) {
        if (text == null) {
            return null;
        }
        return java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[đĐ]", "d");
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

        film.setIsDeleted(true);

        //log.info("Soft delete film {}", film.toString());
        Film deletedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(deletedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO updateFilmById(String filmId, FilmRequestDTO filmDTO) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        Film updateFilm = filmMapper.convertToEntity(filmDTO, film);
        return filmMapper.convertToDTO(filmRepository.save(updateFilm));
    }

    @Override
    @Transactional
    public FilmResponseDTO activeFilm(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        film.setIsActive(true);

        //log.info("Active film {}", film.toString());
        Film updatedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(updatedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO deactiveFilm(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        film.setIsActive(false);

        //log.info("Deactive film {}", film.toString());
        Film updatedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(updatedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO restoreFilmById(String filmId) {
        Film film = filmRepository.findByIdDefault(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        film.setIsDeleted(false);

        //log.info("Restore film {}", film.toString());
        Film restoredFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(restoredFilm);

    }

    public Double rateFilm(String filmId, RatingRequestDTO ratingRequestDTO) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        int rating = ratingRequestDTO.getRating();
        if (rating < 1 || rating > 5) {
            throw new CustomException("Điểm đánh giá phải có giá trị từ 1 đến 5", HttpStatus.BAD_REQUEST.value());
        }
        film.getRatings().put(ratingRequestDTO.getIdUser(), rating);
        filmRepository.save(film);

        return getRating(filmId);
    }

    public Double getRating(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        Map<String, Integer> ratings = film.getRatings();
        if (ratings.isEmpty()) {
            return 0.0;
        }
        double sum = ratings.values().stream().mapToInt(Integer::intValue).sum();
        return sum / ratings.size();
    }

}
