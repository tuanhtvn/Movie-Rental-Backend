package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FilmRequestDTO {
    @NotBlank(message = "Vui lòng nhập tên phim")
    @Size(max = 255, message = "Tên phim không được vượt quá 255 ký tự")
    @Schema(description = "Tên phim", example = "Bố Già")
    @JsonProperty("FilmName")
    private String filmName;
    @NotBlank(message = "Vui lòng chọn ảnh Thumbnail")
    @Schema(description = "Đường dẫn ảnh Thumbnail", example = "https://example.com/thumbnail.jpg")
    @JsonProperty("ThumbnailUrl")
    private String thumbnailUrl;
}
