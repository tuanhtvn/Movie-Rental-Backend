package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rental.movie.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerResponseDTO extends BaseEntity {
    private String id;
    private String imageUrl;
    @JsonProperty("Film")
    private FilmResponseDTO film;
}
