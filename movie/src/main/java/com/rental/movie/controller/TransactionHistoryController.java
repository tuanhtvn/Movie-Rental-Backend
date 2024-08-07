package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.service.TransactionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/transactionHistory")
public class TransactionHistoryController {
    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @Operation(summary = "Lấy danh sách lịch sử giao dịch", description = "API lấy danh sách lịch sử giao dịch")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/transactions")
    public ResponseEntity<BaseResponse> getTransactions() {
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tải danh sách thành công")
                .data(transactionHistoryService.getAll())
                .build();
        return ResponseEntity.ok(response);
    }
}
