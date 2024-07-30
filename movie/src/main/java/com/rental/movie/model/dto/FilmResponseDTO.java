package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilmResponseDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("FilmName")
    private String filmName;
    @JsonProperty("FilmlUrl")
    private String filmUrl;
    @JsonProperty("ThumbnailUrl")
    private String thumbnailUrl;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Rating")
    private Double rating;
}
