package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentedFilmResponseDTO {
    private String id;
    @JsonProperty("Film")
    private FilmResponseDTO film; // phim
    private ZonedDateTime expirationDate; // ngày hết hạn(30 ngày sau ngày thuê)
    private ZonedDateTime expireAt; // thời gian hết hạn(48h kể từ thời điểm xem phim)
    private ZonedDateTime rentalDate; // ngày thuê
    private long minutesLeft; //số phút còn lại
}
