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
    private String id; // khóa chính
    @DBRef(lazy=true)
    private Film film; // phim
    private ZonedDateTime expirationDate; // ngày hết hạn(30 ngày sau ngày thuê)
    private ZonedDateTime expireAt; // thời gian hết hạn(48h kể từ thời điểm xem phim)
    private ZonedDateTime rentalDate; // ngày thuê
}
