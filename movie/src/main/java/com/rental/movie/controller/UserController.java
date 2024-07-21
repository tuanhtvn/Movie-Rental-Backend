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
import com.rental.movie.model.dto.UserInfoRequestDTO;
import com.rental.movie.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Lấy thông tin cá nhân", description = "API xem thông tin cá nhân")
    @ApiResponse(responseCode = "200", description = "Tải thông tin cá nhân thành công")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse> getUser() {
        BaseResponse response = new BaseResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Tải thông tin cá nhân thành công");
        response.setData(userService.getUserInfo());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật thông tin cá nhân", description = "API chỉnh sửa thông tin cá nhân")
    @ApiResponse(responseCode = "200", description = "Cập nhật thông tin cá nhân thành công")
    @PutMapping("/me")
    public ResponseEntity<BaseResponse> updateUserInfo(@RequestBody @Valid UserInfoRequestDTO userInfoRequestDTO) {
        BaseResponse response = new BaseResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Cập nhật thông tin cá nhân thành công");
        response.setData(userService.updateUserInfo(userInfoRequestDTO));
        return ResponseEntity.ok(response);
    }
}
