package com.rental.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.ChangePassRequestDTO;
import com.rental.movie.model.dto.UserInfoRequestDTO;
import com.rental.movie.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api")
@Tag(name = "User Controller")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Lấy thông tin cá nhân", description = "API xem thông tin cá nhân")
    @ApiResponse(responseCode = "200", description = "Tải thông tin cá nhân thành công")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse> getUser() {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tải thông tin cá nhân thành công")
                .data(userService.getUserInfo())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật thông tin cá nhân", description = "API chỉnh sửa thông tin cá nhân")
    @ApiResponse(responseCode = "200", description = "Cập nhật thông tin cá nhân thành công")
    @PutMapping("/me")
    public ResponseEntity<BaseResponse> updateUserInfo(@RequestBody @Valid UserInfoRequestDTO userInfoRequestDTO) {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật thông tin cá nhân thành công")
                .data(userService.updateUserInfo(userInfoRequestDTO))
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Đổi mật khẩu", description = "API đổi mật khẩu")
    @ApiResponse(responseCode = "200", description = "Đổi mật khẩu thành công")
    @PutMapping("/password")
    public ResponseEntity<BaseResponse> changePassword(@RequestBody @Valid ChangePassRequestDTO changePassRequestDTO) {
        userService.changePassword(changePassRequestDTO);
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Đổi mật khẩu thành công")
                .build();
        return ResponseEntity.ok(response);
    }
}
