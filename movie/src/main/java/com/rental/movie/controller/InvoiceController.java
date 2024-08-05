package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @Operation(summary = "Lấy danh sách thông hóa đơn", description = "API lấy danh sách hóa đơn")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @GetMapping("/invoices")
    public ResponseEntity<BaseResponse> getInvoices() {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tải danh sách thành công")
                .data(invoiceService.getAll())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy thông tin hóa đơn bằng id", description = "API lấy thông tin hóa đơn theo id")
    @ApiResponse(responseCode = "200", description = "Lấy thành công ")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @GetMapping("/{invoiceId}")
    public ResponseEntity<BaseResponse> getProfile(@PathVariable String invoiceId) {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin thành công")
                .data(invoiceService.get(invoiceId))
                .build();
        return ResponseEntity.ok(response);
    }
}
