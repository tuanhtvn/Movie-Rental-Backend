package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.common.Role;
import com.rental.movie.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserCreationDTO extends BaseResponseDTO {
    @NotEmpty(message = "Vui lòng nhập họ tên")
    @Schema(description = "Họ tên", example = "Huỳnh Thanh Tuấn")
    @JsonProperty("FullName")
    private String fullName;

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

    @NotEmpty(message = "Vui lòng chọn phương thức đăng nhập")
    @Schema(description = "Phương thức đăng nhập", example = "LOCAL")
    @JsonProperty("AuthProvider")
    private AuthProvider authProvider;

    @NotEmpty(message = "Vui lòng chọn quyền người dùng")
    @Schema(description = "Quyền người dùng", example = "USER")
    @JsonProperty("Role")
    private Role role;

}
