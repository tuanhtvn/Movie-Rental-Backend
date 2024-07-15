package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotEmpty(message = "Vui lòng nhập email")
    @Email(message = "Vui lòng nhập đúng định dạng email")
    @JsonProperty("Email")
    private String email;
    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    @JsonProperty("Password")
    private String password;
}
