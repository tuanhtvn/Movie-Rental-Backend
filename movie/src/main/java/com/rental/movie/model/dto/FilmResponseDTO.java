package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.rental.movie.common.RentalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class FilmResponseDTO extends BaseRequestDTO{
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
    private ZonedDateTime releaseDate;
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
    private Double rating;
    @JsonProperty("Age")
    private Integer age;
    @JsonProperty("Price")
    private Double price;
    @JsonProperty("LimitTime")
    private Integer limitTime;
    @JsonProperty("RentalType")
    private RentalType rentalType;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;
    @JsonProperty("Subtitle")
    private List<SubtitleResponseDTO> subtitles = new ArrayList<>();
    @JsonProperty("Narration")
    private List<NarrationResponseDTO> narrations = new ArrayList<>();
    @JsonProperty("Comment")
    private List<CommentResponseDTO> comments = new ArrayList<>();
    @JsonProperty("Genre")
    private List<GenreResponseDTO> genres = new ArrayList<>();
}
