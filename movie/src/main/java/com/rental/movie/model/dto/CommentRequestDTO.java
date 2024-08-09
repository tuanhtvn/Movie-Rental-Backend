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
    @Schema(description = "Người dùng bình luận", example = "669cc7e87d5a4d563f8b60e7")
    @JsonProperty("IdUser")
    @NotBlank(message = "ID hồ sơ không được để trống")
    private String idUser;
    @Schema(description = "Bình luận của phim", example = "66b10fe0c01aaf371f3cf96e")
    @JsonProperty("FilmId")
    @NotBlank(message = "ID của phim không được để trống")
    private String filmId;
}
