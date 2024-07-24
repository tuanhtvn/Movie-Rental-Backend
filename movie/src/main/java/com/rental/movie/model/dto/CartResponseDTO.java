package com.rental.movie.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartResponseDTO {
    private List<FilmDTO> films;

    @Data
    public static class FilmDTO {
        private String id;
        private String filmName;
        private String filmUrl;
        private String description;
        private String thumbnailUrl;
        private String trailerUrl;
        private String duration;
    }
}
