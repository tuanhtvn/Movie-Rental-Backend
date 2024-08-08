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
    @Schema(description = "Nhập id của phim", example = "66b23d8789c70b58fd667ec4")
    @NotBlank(message = "Chưa có id của phim")
    private String filmId;
    @Schema(description = "Chọn số sao đánh giá", example = "4")
    @NotBlank(message = "Chưa có đánh giá")
    private Integer rating;
    @Schema(description = "Nhập id của người dùng", example = "669b722eb91a812ba752b960")
    @NotBlank(message = "Chưa có id của người dùng")
    private String idUser;
}
