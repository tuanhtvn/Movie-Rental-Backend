package com.rental.movie.model.dto;

import com.rental.movie.model.entity.BaseEntity;
import com.rental.movie.model.entity.Film;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO extends BaseEntity {
    private String id;
    private String imageUrl;
    private Film film;
}
