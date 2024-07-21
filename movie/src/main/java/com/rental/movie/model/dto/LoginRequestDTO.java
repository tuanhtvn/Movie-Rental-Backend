package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotEmpty(message = "Vui lòng nhập email")
    @Email(message = "Vui lòng nhập đúng định dạng email")
    @Schema(description = "Email", example = "rentalmovie68@gmail.com")
    @JsonProperty("Email")
    private String email;

    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    @Schema(description = "Mật khẩu" , example = "123456")
    @JsonProperty("Password")
    private String password;
}
