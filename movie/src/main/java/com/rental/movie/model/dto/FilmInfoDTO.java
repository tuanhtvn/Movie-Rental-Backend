package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class FilmInfoDTO {
    @JsonProperty("FilmName")
    private String filmName;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("TrailerUrl")
    private String trailerUrl;
    @JsonProperty("ReleaseDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private String releaseDate;
    @JsonProperty("Director")
    private String director;
    @JsonProperty("Language")
    private String language;
    @JsonProperty("Age")
    private Integer age;
    @JsonProperty("Price")
    private Double price;

}
