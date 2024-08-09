package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

import com.rental.movie.common.RentalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilmResponseDTO extends BaseResponseDTO{
    @JsonProperty("Id")
    private String id;
    @JsonProperty("FilmName")
    private String filmName;
    @JsonProperty("FilmUrl")
    private String filmUrl;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("ThumbnailUrl")
    private String thumbnailUrl;
    @JsonProperty("TrailerUrl")
    private String trailerUrl;
    @JsonProperty("ReleaseDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private String releaseDate;
    @JsonProperty("Duration")
    private String duration;
    @JsonProperty("Actors")
    private String actors;
    @JsonProperty("Director")
    private String director;
    @JsonProperty("Language")
    private String language;
    @JsonProperty("NumberOfViews")
    private Integer numberOfViews;
    @JsonProperty("Rating")
    private Map<String, Integer> ratings;
    @JsonProperty("Age")
    private Integer age;
    @JsonProperty("Price")
    private Double price;
    @JsonProperty("LimitTime")
    private Integer limitTime;
    @JsonProperty("RentalType")
    private RentalType rentalType;
    private List<SubtitleResponseDTO> subtitles = new ArrayList<>();
    private List<NarrationResponseDTO> narrations = new ArrayList<>();
    private List<CommentResponseDTO> comments = new ArrayList<>();
    private List<GenreResponseDTO> genres = new ArrayList<>();

}
