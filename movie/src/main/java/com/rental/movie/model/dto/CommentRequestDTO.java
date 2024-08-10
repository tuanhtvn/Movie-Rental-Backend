package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequestDTO {
    @Schema(description = "Nội dung bình luận", example = "Phim rất hay!")
    @JsonProperty("Text")
    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String text;
    @Schema(description = "Người dùng bình luận", example = "6694a5cbe8df6594e88ee705")
    @JsonProperty("IdUser")
    @NotBlank(message = "ID hồ sơ không được để trống")
    private String idUser;
    @Schema(description = "Bình luận của phim", example = "66b6e524dc337e7b7304dad7")
    @JsonProperty("FilmId")
    @NotBlank(message = "ID của phim không được để trống")
    private String filmId;
}
