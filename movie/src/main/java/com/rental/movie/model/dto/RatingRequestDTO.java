package com.rental.movie.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequestDTO {

    private String filmId;
    @Schema(description = "Chọn số sao đánh giá", example = "4")
    @NotBlank(message = "Bạn chưa chọn số sao tương ứng")
    private int rating;
}
