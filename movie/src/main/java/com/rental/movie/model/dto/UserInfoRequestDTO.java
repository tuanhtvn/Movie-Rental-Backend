package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserInfoRequestDTO {
    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(max = 50, message = "Họ tên không được vượt quá 50 ký tự")
    @Schema(description = "Họ tên", example = "Huỳnh Thanh Tuấn")
    @JsonProperty("FullName")
    private String fullName;

    @NotBlank(message = "Vui lòng chọn ảnh đại diện")
    @Schema(description = "Ảnh đại diện", example = "https://res.cloudinary.com/dgpfsipnc/image/upload/v1723042655/rcs1oldxokpbhqbseqxx.jpg")
    @JsonProperty("Avatar")
    private String avatar;
}
