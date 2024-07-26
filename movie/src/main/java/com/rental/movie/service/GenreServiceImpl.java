package com.rental.movie.service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.common.Role;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.GenreRequestDTO;
import com.rental.movie.model.dto.GenreResponseDTO;
import com.rental.movie.model.entity.Genre;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.GenreRepository;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.util.mapper.GenreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class GenreServiceImpl implements GenreService {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    private IAuthentication authManager;

    @Override
    public Page<GenreResponseDTO> getAll(Pageable pageable, String search) {
        Page<Genre> genres = genreRepository.findAll(pageable, search);
        log.info("Get all genres: {}", genres.getContent().toString());
        if(genres.isEmpty()) {
            log.error("No genres found");
            throw new CustomException("Không có thể loại nào", HttpStatus.NOT_FOUND.value());
        }
        return genreMapper.convertToDTO(genres);
    }

    @Override
    public Page<GenreResponseDTO> getAllActiveGenres(Pageable pageable, String search) {
        Page<Genre> genres = genreRepository.findAllActive(pageable, search);
        log.info("Get all active genres: {}", genres.getContent().toString());
        if(genres.isEmpty()){
            log.error("No active genres found");
            throw new CustomException("Không có thể loại nào", HttpStatus.NOT_FOUND.value());
        }
        return genreMapper.convertToDTO(genres);
    }

    @Override
    public Page<GenreResponseDTO> getAllInactiveGenres(Pageable pageable, String search) {
        Page<Genre> genres = genreRepository.findAllInActive(pageable, search);
        log.info("Get all inactive genres: {}", genres.getContent().toString());
        if(genres.isEmpty()){
            log.error("No inactive genres found");
            throw new CustomException("Không có thể loại nào", HttpStatus.NOT_FOUND.value());
        }
        return genreMapper.convertToDTO(genres);
    }

    @Override
    public Page<GenreResponseDTO> getAllSoftDeletedGenres(Pageable pageable, String search) {
        Page<Genre> genres = genreRepository.findAllSoftDelete(pageable, search);
        log.info("Get all soft delete genres: {}", genres.getContent().toString());
        if(genres.isEmpty()){
            log.error("No soft delete albums found");
            throw new CustomException("Không tìm thấy thể loại nào", HttpStatus.NOT_FOUND.value());
        }
        return genreMapper.convertToDTO(genres);
    }

    @Override
    public GenreResponseDTO getGenreById(String GenreId) {
        User user = authManager.getUserAuthentication();
        Genre genre = genreRepository.findById(GenreId)
                .orElseThrow(() -> {
                    log.error("Genre not found");
                    return new CustomException("Không tìm thấy thể loại", HttpStatus.NOT_FOUND.value());
                });
        if(user.getRole().equals(Role.USER) && genre.getIsActive().equals(false)) {
            log.error("User get inactive genre");
            throw new CustomException("Bạn không có quyền truy cập thể loại", HttpStatus.METHOD_NOT_ALLOWED.value());
        }
        log.info("Get genre by id {}", genre.toString());
        return genreMapper.convertToDTO(genre);
    }

    @Override
    @Transactional
    public GenreResponseDTO createGenre(GenreRequestDTO GenreDTO) {
        Genre genre = genreMapper.convertToEntity(GenreDTO);
        log.info("Create genre {}", genre.toString());
        return genreMapper.convertToDTO(genreRepository.save(genre));
    }

    @Override
    @Transactional
    public GenreResponseDTO updateGenre(
            String GenreId,
            GenreRequestDTO GenreDTO
    ) {
        Genre genre = genreRepository.findById(GenreId)
                .orElseThrow(() -> {
                    log.error("Genre not found");
                    return new CustomException("Thể loại không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        boolean anyFilmNotFound = GenreDTO.getFilmsId().stream().anyMatch(
                (filmId) -> !filmRepository.existsById(filmId)
        );
        if(anyFilmNotFound) {
            log.error("Film not found");
            throw new CustomException("Phim không tồn tại", HttpStatus.NOT_FOUND.value());
        }
        log.info("Update genre {}", genre.toString());
        genre = genreMapper.convertToEntity(GenreDTO, genre);
        return genreMapper.convertToDTO(genreRepository.save(genre));
    }

    @Override
    @Transactional
    public void softDeleteGenre(String GenreId) {
        Genre genre = genreRepository.findById(GenreId)
                .orElseThrow(() -> {
                    log.error("Genre not found");
                    return new CustomException("ID thể loại không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        genre.setIsDeleted(true);
        genreRepository.save(genre);
        log.info("Soft delete genre {}", genre.toString());
    }

    @Override
    @Transactional
    public void restoreGenre(String GenreId) {
        Genre genre = genreRepository.findById(GenreId)
                .orElseThrow(() -> {
                    log.error("Genre not found");
                    return new CustomException("ID thể loại không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        genre.setIsDeleted(false);
        genreRepository.save(genre);
        log.info("Restore genre {}", genre.toString());
    }

    @Override
    @Transactional
    public void activateGenre(String GenreId) {
        Genre genre = genreRepository.findById(GenreId)
                .orElseThrow(() -> {
                    log.error("Genre not found");
                    return new CustomException("ID thể loại không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        genre.setIsActive(true);
        genreRepository.save(genre);
        log.info("Activate genre {}", genre.toString());
    }

    @Override
    @Transactional
    public void deactivateGenre(String GenreId) {
        Genre genre = genreRepository.findById(GenreId)
                .orElseThrow(() -> {
                    log.error("Genre not found");
                    return new CustomException("ID thể loại không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        genre.setIsActive(false);
        genreRepository.save(genre);
        log.info("Deactivate genre {}", genre.toString());
    }
}
