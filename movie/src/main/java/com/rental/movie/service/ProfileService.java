package com.rental.movie.service;

import java.util.List;

import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.ProfileRequestDTO;
import com.rental.movie.model.dto.ProfileResponseDTO;

public interface ProfileService {
    public void create(ProfileRequestDTO profileRequestDTO);

    public List<ProfileResponseDTO> getAll();

    public ProfileResponseDTO get(String id);

    public void update(String id, ProfileRequestDTO profileRequestDTO);

    public void delete(String id);

    public void pushFilm(String profileId, String filmId);

    public void popFilm(String profileId, String filmId);

    public List<FilmResponseDTO> getAllFilm(String profileId);
}
