package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.RentedFilmResponseDTO;
import com.rental.movie.model.entity.Film;
import com.rental.movie.model.entity.RentedFilm;
import com.rental.movie.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RentedFilmMapper {

    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private FilmMapper filmMapper;

    public RentedFilmResponseDTO convertToDTO(RentedFilm rentedFilm){
        RentedFilmResponseDTO responseDTO = new RentedFilmResponseDTO();
        if (rentedFilm.getFilm() != null){
            Film film = filmRepository.findByIdDefault(rentedFilm.getFilm().getId()).orElse(null);
            if (film != null) {
                FilmResponseDTO responseFilm = filmMapper.convertToDTO(film);
                responseDTO.setFilm(responseFilm);
            }
        }

        responseDTO.setId(rentedFilm.getId());
        responseDTO.setExpireAt(rentedFilm.getExpireAt());
        responseDTO.setRentalDate(rentedFilm.getRentalDate());
        responseDTO.setExpirationDate(rentedFilm.getExpirationDate());
        responseDTO.setMinutesLeft(rentedFilm.getMinutesLeft());

        return responseDTO;
    }
}
