package com.rental.movie.controller;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.model.dto.SupportRequestDTO;
import com.rental.movie.model.dto.SupportRequestResponseDTO;
import com.rental.movie.model.entity.User;
import com.rental.movie.service.SupportRequestService;
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
@RequestMapping("/api/support-requests")
public class SupportRequestController {
    private final SupportRequestService supportRequestService;

    @Autowired
    private IAuthentication authentication;

    public SupportRequestController(SupportRequestService supportRequestService) {
        this.supportRequestService = supportRequestService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Tạo yêu cầu hỗ trợ",description = "Tạo yêu cầu hỗ trợ mới")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Yêu cầu hỗ trợ đã được tạo"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu hỗ trợ chưa được tạo")
    })
    @PostMapping("/create")
    public ResponseEntity<SupportRequestDTO> createSupportRequest(@RequestBody SupportRequestDTO supportRequestDTO) {
        User user = authentication.getUserAuthentication();
        supportRequestDTO.setUserId(user.getId());
        SupportRequestDTO createdRequest = supportRequestService.createSupportRequest(supportRequestDTO);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy tất cả yêu cầu hỗ trợ",description = "Lấy danh sách tất cả yêu cầu hỗ trợ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Danh sách yêu cầu hỗ trợ đã được tìm thấy"),
            @ApiResponse(responseCode = "404", description = "Danh sách yêu cầu hỗ trợ không tồn tại")
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<SupportRequestResponseDTO>> getAllSupportRequests() {
        List<SupportRequestResponseDTO> supportRequests = supportRequestService.getAllSupportRequests();
        return ResponseEntity.ok(supportRequests);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy yêu cầu hỗ trợ theo ID người dùng",description = "Lấy danh sách yêu cầu hỗ trợ theo ID người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Danh sách yêu cầu hỗ trợ đã được tìm thấy"),
            @ApiResponse(responseCode = "404", description = "Danh sách yêu cầu hỗ trợ không tồn tại")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<List<SupportRequestResponseDTO>> getSupportRequestsByUserId(@PathVariable String userId) {
        List<SupportRequestResponseDTO> supportRequests = supportRequestService.getSupportRequestsByUserId(userId);
        return ResponseEntity.ok(supportRequests);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy tất cả yêu cầu hỗ trợ chưa giải quyết",description = "Lấy danh sách tất cả yêu cầu hỗ trợ chưa giải quyết")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Danh sách yêu cầu hỗ trợ chưa giải quyết đã được tìm thấy"),
            @ApiResponse(responseCode = "404", description = "Danh sách yêu cầu hỗ trợ chưa giải quyết không tồn tại")
    })
    @GetMapping("/unresolved")
    public ResponseEntity<List<SupportRequestResponseDTO>> getUnresolvedSupportRequests() {
        List<SupportRequestResponseDTO> supportRequests = supportRequestService.getUnresolvedSupportRequests();
        return ResponseEntity.ok(supportRequests);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Xóa yêu cầu hỗ trợ",description = "Xóa yêu cầu hỗ trợ theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Yêu cầu hỗ trợ đã được xóa"),
            @ApiResponse(responseCode = "404", description = "Yêu cầu hỗ trợ không tồn tại")
    })
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteSupportRequest(@PathVariable String id) {
        supportRequestService.deleteSupportRequest(id);
        return ResponseEntity.noContent().build();
    }

}