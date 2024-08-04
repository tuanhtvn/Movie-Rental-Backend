package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class FilmResponseDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("FilmName")
    private String filmName;
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
    @JsonProperty("Subtitles")
    private List<SubtitleResponseDTO> subtitleId = new ArrayList<>();
    @JsonProperty("Narration")
    private List<NarrationResponseDTO> narrationId = new ArrayList<>();
    @JsonProperty("Comment")
    private List<CommentResponseDTO> commentId = new ArrayList<>();
}
