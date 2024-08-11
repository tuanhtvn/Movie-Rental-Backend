package com.rental.movie.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.dto.NarrationRequestDTO;
import com.rental.movie.model.dto.NarrationResponseDTO;
import com.rental.movie.model.entity.Narration;
import com.rental.movie.model.entity.Film;

public interface NarrationService {
    public FilmResponseDTO getFilmById(String id);
    public Page<FilmResponseDTO> getAllActivedFilm(Pageable pageable, String search);
    public NarrationResponseDTO createNarration(NarrationRequestDTO narrationRequestDTO);
    public NarrationResponseDTO updateNarration(String id, NarrationRequestDTO narrationRequestDTO);
    public void deleteNarration(String narrationId);
    public FilmResponseDTO addNarration(String narrationId, String filmId);
    public FilmResponseDTO removeNarration(String narrationId, String filmId);
    public List<NarrationResponseDTO> getAllNarrations();
    public List<NarrationResponseDTO> getAllNarrationsByFilmId(String filmId);
}
