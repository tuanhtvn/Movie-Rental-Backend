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

    public Film convertToEntity(FilmRequestDTO filmDTO) {
        Film film = modelMapper.map(filmDTO, Film.class);
        // Map Genres
        film.setGenres(filmDTO.getGenresId().stream().map(id -> {
            return genreRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy Genre", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        // Map Subtitles
        film.setSubtitles(filmDTO.getSubtitlesId().stream().map(id -> {
            return subtitleRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy Subtitle", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        // Map Narrations
        film.setNarrations(filmDTO.getNarrationsId().stream().map(id -> {
            return narrationRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy Narration", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        // Map Comments
        film.setComments(filmDTO.getCommentsId().stream().map(id -> {
            return commentRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy Comment", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        return film;
    }

    // Overloading for update Film
    public Film convertToEntity(FilmRequestDTO dto, Film film) {
        modelMapper.map(dto, film);
        // Map Genres
        film.setGenres(dto.getGenresId().stream().map(id -> {
            return genreRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy Genre", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        // Map Subtitles
        film.setSubtitles(dto.getSubtitlesId().stream().map(id -> {
            return subtitleRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy Subtitle", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        // Map Narrations
        film.setNarrations(dto.getNarrationsId().stream().map(id -> {
            return narrationRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy Narration", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        // Map Comments
        film.setComments(dto.getCommentsId().stream().map(id -> {
            return commentRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy Comment", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        return film;
    }
}
