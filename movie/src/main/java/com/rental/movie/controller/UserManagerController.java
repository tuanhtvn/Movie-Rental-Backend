package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.UserCreationDTO;
import com.rental.movie.model.dto.UserInfoResponseDTO;
import com.rental.movie.service.UserManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Operation(summary = "Tạo người dùng", description = "Tạo người dùng mới")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Người dùng đã được tạo"),
            @ApiResponse(responseCode = "404", description = "Tạo người dùng thất bại")
    })
    @PostMapping("/create")
    public ResponseEntity<UserCreationDTO> createUser(@RequestBody UserCreationDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update người dùng", description = "Update người dùng mới")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Người dùng đã được update"),
            @ApiResponse(responseCode = "404", description = "Update người dùng thất bại")
    })
    @PutMapping("/{id}/update")
    public ResponseEntity<UserCreationDTO> updateUser(@PathVariable String id, @RequestBody UserCreationDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Lấy tất cả người dùng", description = "Lấy danh sách tất cả người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Danh sách người dùng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<UserCreationDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Xóa người dùng", description = "Xóa người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Người dùng đã được xóa"),
            @ApiResponse(responseCode = "404", description = "Xóa người dùng thất bại")
    })
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Kích hoạt người dùng", description = "Kích hoạt người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Người dùng đã được kích hoạt"),
            @ApiResponse(responseCode = "404", description = "Kích hoạt người dùng thất bại")
    })
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable String id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Deactivate người dùng", description = "Deactivate người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Người dùng đã được deactivate"),
            @ApiResponse(responseCode = "404", description = "Deactivate người dùng thất bại")
    })
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable String id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Xóa mềm người dùng", description = "Xóa mềm người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Người dùng đã được xóa mềm"),
            @ApiResponse(responseCode = "404", description = "Xóa mềm người dùng thất bại")
    })
    @PutMapping("/{id}/soft-delete")
    public ResponseEntity<Void> softDeleteUser(@PathVariable String id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
