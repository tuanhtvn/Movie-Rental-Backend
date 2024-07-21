package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ForgotPasswordRequestDTO {
    @NotEmpty(message = "Vui lòng nhập email")
    @Email(message = "Vui lòng nhập đúng định dạng email")
    @Schema(description = "Email", example = "huynhthanhtuan2206@gmail.com")
    @JsonProperty("Email")
    private String email;

    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-+]).{8,20}$", message = "Mật khẩu phải chứa ít nhất 1 chữ số, 1 chữ thường, 1 chữ hoa, 1 ký tự đặc biệt và có độ dài từ 8-20 ký tự")
    @Schema(description = "Mật khẩu", example = "Example@123")
    @JsonProperty("Password")
    private String password;

    @NotEmpty(message = "Vui lòng nhập lại mật khẩu")
    @Schema(description = "Nhập lại mật khẩu", example = "Example@123")
    @JsonProperty("PasswordConfirm")
    private String passwordConfirm;
}
