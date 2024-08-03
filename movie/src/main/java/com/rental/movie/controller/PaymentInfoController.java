package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.PaymentInfoRepuestDTO;
import com.rental.movie.service.PaymentInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment-info")
public class PaymentInfoController {
    @Autowired
    private PaymentInfoService paymentInfoService;

    @Operation(summary = "Lấy danh sách thông tin thanh toán", description = "API lấy danh sách thông tin thanh toán")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/paymentInfos")
    public ResponseEntity<BaseResponse> getPaymentInfos() {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tải danh sách thành công")
                .data(paymentInfoService.getAll())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy thông tin thanh toán bằng id thông tin thanh toán", description = "API lấy thông tin thanh toán theo id")
    @ApiResponse(responseCode = "200", description = "Lấy thành công thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/paymentInfos/{paymentInfoId}")
    public ResponseEntity<BaseResponse> getProfile(@PathVariable("paymentInfoId") String paymentInfoId) {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin thành công")
                .data(paymentInfoService.get(paymentInfoId))
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy danh sách thông tin thanh toán xóa mềm", description = "API lấy danh sách thông tin thanh toán xóa mềm")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/softDeletedPaymentInfos")
    public ResponseEntity<BaseResponse> getAllSoftDeletedPaymentInfos() {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tải danh sách thành công")
                .data(paymentInfoService.getAllSoftDelete())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy danh sách thông tin thanh toán", description = "API lấy danh sách thông tin thanh toán Active")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/activePaymentInfos")
    public ResponseEntity<BaseResponse> getAllActivePaymentInfos() {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tải danh sách thành công")
                .data(paymentInfoService.getAllActive())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy danh sách thông tin thanh toán inactive", description = "API lấy danh sách thông tin thanh toán Inactive")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/inActivePaymentInfos")
    public ResponseEntity<BaseResponse> getAllInActivePaymentInfos() {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tải danh sách thành công")
                .data(paymentInfoService.getAllInActive())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Thêm thông tin thanh toán", description = "API thêm thông tin thanh toán")
    @ApiResponse(responseCode = "201", description = "Thêm thông tin thanh toán thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/paymentInfo")
    public ResponseEntity<BaseResponse> createPayment(@RequestBody @Valid PaymentInfoRepuestDTO paymentInfoRepuestDTO) {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Thêm thông tin thanh toán thành công")
                .data(paymentInfoService.addPaymentInfo(paymentInfoRepuestDTO))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @Operation(summary = "Cập nhật thông tin thanh toán", description = "API cập nhật thông tin thanh toán")
    @ApiResponse(responseCode = "200", description = "Cập nhật thông tin thanh toán thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/paymentInfo/{paymentInfoId}")
    public ResponseEntity<BaseResponse> updateProfile(@PathVariable("paymentInfoId") String paymentInfoId,
                                                      @RequestBody @Valid PaymentInfoRepuestDTO paymentInfoRepuestDTO) {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật thông tin thanh toán thành công")
                .data(paymentInfoService.update(paymentInfoId,paymentInfoRepuestDTO))
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xoá mềm thông tin thanh toán", description = "Xoá mềm thông tin thanh toán theo Id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin thanh toán!"),
            @ApiResponse(responseCode = "200", description = "Xoá mềm thành công.")
    })
    @PatchMapping("/softDelete/{paymentInfoId}")
    public ResponseEntity<BaseResponse> softDeleteById(@PathVariable String paymentInfoId) {
        paymentInfoService.softDeletePaymentInfo(paymentInfoId);
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xóa hồ sơ thành công")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Khôi phục thông tin thanh toán", description = "Khôi phục thông tin thanh toán theo Id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin thanh toán!"),
            @ApiResponse(responseCode = "200", description = "Khôi phục thành công.")
    })
    @PatchMapping("/restore/{paymentInfoId}")
    public ResponseEntity<BaseResponse> restoreById(@PathVariable String paymentInfoId) {
        paymentInfoService.restorePaymentInfo(paymentInfoId);
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Khôi phục thành công")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Activate thông tin thanh toán", description = "Activate thông tin thanh toán theo Id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin thanh toán!"),
            @ApiResponse(responseCode = "200", description = "Active thành công.")
    })
    @PatchMapping("/activate/{paymentInfoId}")
    public ResponseEntity<BaseResponse> activateById(@PathVariable String paymentInfoId) {
        paymentInfoService.activatePaymentInfo(paymentInfoId);
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Activate thành công")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deactivate thông tin thanh toán", description = "Deactivate thông tin thanh toán theo Id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin thanh toán!"),
            @ApiResponse(responseCode = "200", description = "Deactivate thành công.")
    })
    @PatchMapping("/deactivate/{paymentInfoId}")
    public ResponseEntity<BaseResponse> deactivateById(@PathVariable String paymentInfoId) {
        paymentInfoService.deAvtivatePaymentInfo(paymentInfoId);
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Deactivate thành công")
                .build();
        return ResponseEntity.ok(response);
    }
}
