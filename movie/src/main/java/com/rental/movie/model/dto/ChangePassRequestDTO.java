package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePassRequestDTO {
    @NotEmpty(message = "Vui lòng nhập mật khẩu cũ")
    @Schema(description = "Mật khẩu cũ", example = "Example@111")
    @JsonProperty("PasswordOld")
    private String passwordOld;

    @NotEmpty(message = "Vui lòng nhập mật khẩu mới")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-+]).{8,20}$", message = "Mật khẩu phải chứa ít nhất 1 chữ số, 1 chữ thường, 1 chữ hoa, 1 ký tự đặc biệt và có độ dài từ 8-20 ký tự")
    @Schema(description = "Mật khẩu mới", example = "Example@123")
    @JsonProperty("PasswordNew")
    private String passwordNew;

    @NotEmpty(message = "Vui lòng xác nhận mật khẩu")
    @Schema(description = "Nhập lại mật khẩu mới", example = "Example@123")
    @JsonProperty("PasswordConfirm")
    private String passwordConfirm;
}
