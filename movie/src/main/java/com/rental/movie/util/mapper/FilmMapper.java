package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.model.entity.Film;
import com.rental.movie.repository.GenreRepository;
import com.rental.movie.repository.SubtitleRepository;
import com.rental.movie.repository.NarrationRepository;
import com.rental.movie.repository.CommentRepository;
import com.rental.movie.exception.CustomException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FilmMapper {
    @Autowired
    private ModelMapper modelMapper;
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

    public FilmResponseDTO convertToDTO(Film film) {
        return modelMapper.map(film, FilmResponseDTO.class);
    }

    public Page<FilmResponseDTO> convertToDTO(Page<Film> films) {
        return films.map(this::convertToDTO);
    }

    public Film convertToEntity(FilmRequestDTO dto) {
        Film film = modelMapper.map(dto, Film.class);
        film.setGenresId(
                dto.getGenresId() == null ? null : dto.getGenresId().stream().map(genreId -> {
                    genreRepository.findById(genreId).orElseThrow(
                            () -> new CustomException("Không tìm thấy thể loại", HttpStatus.NOT_FOUND.value()));
                    return genreId;
                }).collect(Collectors.toList())
        );
        film.setSubtitles(mapIdsToEntities(dto.getSubtitlesId(), subtitleRepository::findById, "Không tìm thấy Subtitle"));
        film.setNarrations(mapIdsToEntities(dto.getNarrationsId(), narrationRepository::findById, "Không tìm thấy Narration"));
        film.setActors(dto.getActors());
        return film;
    }

    // Overloading for update Film
    public Film convertToEntity(FilmRequestDTO dto, Film film) {
        modelMapper.map(dto, film);
        film.setGenresId(
                dto.getGenresId() == null ? null : dto.getGenresId().stream().map(genreId -> {
                    genreRepository.findById(genreId).orElseThrow(
                            () -> new CustomException("Không tìm thấy thể loại", HttpStatus.NOT_FOUND.value()));
                    return genreId;
                }).collect(Collectors.toList())
        );
        film.setSubtitles(mapIdsToEntities(dto.getSubtitlesId(), subtitleRepository::findById, "Không tìm thấy Subtitle"));
        film.setNarrations(mapIdsToEntities(dto.getNarrationsId(), narrationRepository::findById, "Không tìm thấy Narration"));
        film.setActors(dto.getActors());
        return film;
    }

    private <T> List<T> mapIdsToEntities(List<String> ids, Function<String, Optional<T>> repositoryMethod, String errorMessage) {
        if (ids == null) {
            return Collections.emptyList();
        }
        return ids.stream()
                .map(id -> repositoryMethod.apply(id)
                        .orElseThrow(() -> new CustomException(errorMessage, HttpStatus.NOT_FOUND.value())))
                .collect(Collectors.toList());
    }
}
