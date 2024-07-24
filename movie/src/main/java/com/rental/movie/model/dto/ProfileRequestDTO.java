package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileRequestDTO {
    @NotBlank(message = "Vui lòng nhập tên hồ sơ")
    @Size(max = 50, message = "Tên hồ sơ không được vượt quá 50 ký tự")
    @Schema(description = "Tên hồ sơ", example = "Mọt phim")
    @JsonProperty("ProfileName")
    private String profileName;
    @NotBlank(message = "Vui lòng chọn ảnh đại diện")
    @Schema(description = "Đường dẫn ảnh đại diện", example = "https://example.com/image.jpg")
    @JsonProperty("ImageUrl")
    private String imageUrl;
    @NotNull(message = "Vui lòng chọn loại hồ sơ")
    @Schema(description = "Loại hồ sơ", example = "false")
    @JsonProperty("IsChild")
    private Boolean isChild;
}
