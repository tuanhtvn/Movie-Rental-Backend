package com.rental.movie.controller;

import com.rental.movie.model.dto.UserInfoResponseDTO;
import com.rental.movie.service.UserManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users-manager")
public class UserManagerController {

    @Autowired
    private UserManagerService userService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update người dùng", description = "Update người dùng mới")
    @ApiResponse(responseCode = "200", description = "Người dùng đã được cập nhật")
    @PutMapping("/{id}/update")
    public ResponseEntity<UserInfoResponseDTO> updateUser(@PathVariable String id, @RequestBody UserInfoResponseDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Tìm người dùng theo id", description = "Tìm người dùng theo id")
    @ApiResponse(responseCode = "200", description = "Người dùng đã được tìm thấy")
    @GetMapping("/{id}/get")
    public ResponseEntity<UserInfoResponseDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Lấy tất cả người dùng", description = "Lấy danh sách tất cả người dùng")
    @ApiResponse(responseCode = "200", description = "Danh sách người dùng đã được tìm thấy")
    @GetMapping("/getAll")
    public ResponseEntity<List<UserInfoResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Xóa người dùng", description = "Xóa người dùng")
    @ApiResponse(responseCode = "200", description = "Người dùng đã được xóa")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Kích hoạt người dùng", description = "Kích hoạt người dùng")
    @ApiResponse(responseCode = "200", description = "Người dùng đã được kích hoạt")
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable String id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Deactivate người dùng", description = "Deactivate người dùng")
    @ApiResponse(responseCode = "200", description = "Người dùng đã được deactivate")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable String id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Xóa mềm người dùng", description = "Xóa mềm người dùng")
    @ApiResponse(responseCode = "200", description = "Người dùng đã được xóa mềm")
    @PutMapping("/{id}/soft-delete")
    public ResponseEntity<Void> softDeleteUser(@PathVariable String id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
