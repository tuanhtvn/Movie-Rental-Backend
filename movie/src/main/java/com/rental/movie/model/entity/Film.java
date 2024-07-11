package com.rental.movie.model.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.rental.movie.common.RentalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document(collection = "Film")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Film extends BaseEntity {
    @Id
    private String id;
    private String filmName;
    private String filmUrl;
    private String description;
    private String thumbnailUrl;
    private String trailerUrl;
    private ZonedDateTime releaseDate;
    private String duration;
    private String actors;
    private String director;
    private String language;
    private Integer numberOfViews;
    private Double rating;
    private Integer age;
    private RentalType rentalType;
    private Double price;
    private Integer limitTime;

    private List<Subtitle> subtitles = new ArrayList<>();
    private List<Narration> narrations = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    @DBRef
    private List<Genre> genres = new ArrayList<>();
}
