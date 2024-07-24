package com.rental.movie.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartRequestDTO {
    @NotBlank(message = "Film ID không được để trống")
    private String filmId;
}
