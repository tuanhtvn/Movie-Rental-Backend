package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyRequestDTO {
    @NotBlank(message = "Vui lòng nhập mã xác minh")
    @Pattern(regexp = "^[0-9]{4}$", message = "Mã xác minh phải có đúng 4 chữ số")
    @Schema(description = "Mã xác minh", example = "1234")
    @JsonProperty("Code")
    private String code;
}
