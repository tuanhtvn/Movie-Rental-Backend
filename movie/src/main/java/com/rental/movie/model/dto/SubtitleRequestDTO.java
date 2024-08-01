package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubtitleRequestDTO {
    @NotBlank(message = "Vui lòng nhập tên phụ đề")
    @Size(max = 100, message = "Tên phụ đề không được vượt quá 100 ký tự")
    @Schema(description = "Tên phụ đề", example = "Phụ đề Tiếng Việt")
    @JsonProperty("SubtitleName")
    private String subtitleName;
    @NotBlank(message = "Vui lòng chọn phụ đề")
    @Schema(description = "Đường dẫn phụ đề", example = "https://example.com/phude.mp3")
    @JsonProperty("SubtitleUrl")
    private String subtitleUrl;
}
