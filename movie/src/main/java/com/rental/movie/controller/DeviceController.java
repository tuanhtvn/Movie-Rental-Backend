package com.rental.movie.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.DeviceResponseDTO;
import com.rental.movie.service.DeviceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/user")
@Tag(name = "Device Controller")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Operation(summary = "Tải danh sách thiết bị đăng nhập", description = "API tải danh sách thiết bị đăng nhập")
    @ApiResponse(responseCode = "200", description = "Tải danh sách thiết bị đăng nhập thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/devices")
    public ResponseEntity<BaseResponse> getAll(HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DeviceResponseDTO> data = deviceService.getAll(request, pageable);
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tải danh sách thiết bị đăng nhập thành công")
                .data(new HashMap<String, Object>() {
                    {
                        put("Devices", data.getContent());
                        put("CurrentPage", data.getNumber());
                        put("NumberOfItems", data.getNumberOfElements());
                        put("TotalItems", data.getTotalElements());
                        put("TotalPages", data.getTotalPages());
                    }
                })
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xoá thiết bị đăng nhập", description = "API xoá thiết bị đăng nhập")
    @ApiResponse(responseCode = "200", description = "Xoá thiết bị đăng nhập thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/device/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable("id") String id, HttpServletRequest request) {
        deviceService.delete(id, request);
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xoá thiết bị đăng nhập thành công")
                .build();
        return ResponseEntity.ok(response);
    }
}
