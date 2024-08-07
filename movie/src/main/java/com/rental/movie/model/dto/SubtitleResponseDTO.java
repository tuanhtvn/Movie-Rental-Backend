package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubtitleResponseDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("SubtitleName")
    private String subtitleName;
    @JsonProperty("SubtitleUrl")
    private String subtitleUrl;
    @JsonProperty("FilmId")
    private String filmId;
}
