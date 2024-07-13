package com.rental.movie.model.dto;

import com.rental.movie.model.entity.Film;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO {
    private String id;
    private String imageUrl;
    private Film film;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
