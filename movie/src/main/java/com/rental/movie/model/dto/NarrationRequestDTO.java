package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NarrationRequestDTO {
    @NotBlank(message = "Vui lòng nhập tên thuyết minh")
    @Size(max = 100, message = "Tên thuyết minh không được vượt quá 100 ký tự")
    @Schema(description = "Tên thuyết minh", example = "Thuyết minh Tiếng Việt")
    @JsonProperty("Language")
    private String language;
    @NotBlank(message = "Vui lòng chọn thuyết minh")
    @Schema(description = "Đường dẫn thuyết minh", example = "https://example.com/narration.mp3")
    @JsonProperty("NarrationUrl")
    private String narrationUrl;
}
