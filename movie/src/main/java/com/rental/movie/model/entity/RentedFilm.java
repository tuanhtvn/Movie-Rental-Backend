package com.rental.movie.model.entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentedFilm {
    @Id
    private String id;
    @DBRef
    private Film film;
    private ZonedDateTime expirationDate;
    private ZonedDateTime expireAt;
    private ZonedDateTime rentalDate;
}
