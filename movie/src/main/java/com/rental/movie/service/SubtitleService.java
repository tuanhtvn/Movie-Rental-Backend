package com.rental.movie.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.InputStream;

import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.dto.SubtitleRequestDTO;
import com.rental.movie.model.dto.SubtitleResponseDTO;
import com.rental.movie.model.entity.Subtitle;
import com.rental.movie.model.entity.Film;

public interface SubtitleService {
    public FilmResponseDTO getFilmById(String id);
    public Page<FilmResponseDTO> getAllActivedFilm(Pageable pageable, String search);
    public SubtitleResponseDTO createSubtitle(SubtitleRequestDTO subtitleRequestDTO);
    public SubtitleResponseDTO updateSubtitle(String id, SubtitleRequestDTO subtitleRequestDTO);
    public void deleteSubtitle(String subtitleId);
    public FilmResponseDTO addSubtitle(SubtitleRequestDTO subtitleDTO, String filmId);
    public FilmResponseDTO removeSubtitle(String subtitleId, String filmId);
    public List<SubtitleResponseDTO> getAllSubtitles();
    public List<SubtitleResponseDTO> getAllSubtitlesByFilmId(String filmId);
    public InputStream getSubtitleStream(String subtitleId) throws Exception;
    public List<Subtitle> findByFilmId(String filmId);
}
