package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.PackageInfoRequestDTO;
import com.rental.movie.service.PackageInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PackageInfoController {
    @Autowired
    private PackageInfoService packageInfoService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Tạo mới gói thuê.")
    @ApiResponse(responseCode = "200", description = "Tạo mới gói thuê thành công.")
    @PostMapping("/package/create")
    public ResponseEntity<BaseResponse> createPackage(@RequestBody @Valid PackageInfoRequestDTO packageInfoRequestDTO) {
        return packageInfoService.createPackage(packageInfoRequestDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách gói thuê, có phân trang.")
    @ApiResponse(responseCode = "200", description = "Thành công.")
    @GetMapping("/package/getAll")
    public ResponseEntity<BaseResponse> getAllPage(@ParameterObject Pageable pageable) {
        return packageInfoService.getAllPackage(pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Cập nhật gói thuê dựa trên id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói thuê."),
            @ApiResponse(responseCode = "200", description = "Cập nhật gói thuê thành công.")
    })
    @PutMapping("/package/update/{id}")
    public ResponseEntity<BaseResponse> updateById(@PathVariable String id,
                                                   @RequestBody @Valid PackageInfoRequestDTO newPkt) {
        return packageInfoService.updatePackageById(id, newPkt);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Xoá mềm gói thuê dựa trên id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói thuê."),
            @ApiResponse(responseCode = "200", description = "Xoá mềm gói thuê thành công.")
    })
    @PatchMapping("/package/softDelete/{id}")
    public ResponseEntity<BaseResponse> softDeleteById(@PathVariable String id) {
        return packageInfoService.softDeletePackageById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Thay đổi trạng thái Active/Inactive cho gói thuê theo id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói thuê."),
            @ApiResponse(responseCode = "200", description = "Cập nhật trạng thái Active/Inactive cho gói thuê thành công.")
    })
    @PutMapping("/package/toggle-active-status/{id}")
    public ResponseEntity<BaseResponse> toggleActiveStatusById(@PathVariable String id) {
        return packageInfoService.toggleActiveStatus(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách gói thuê đã xoá, có phân trang.")
    @ApiResponse(responseCode = "200", description = "Thành công.")
    @GetMapping("/package/getSoftDeleted")
    public ResponseEntity<BaseResponse> getSoftDeletedHasPage(@ParameterObject Pageable pageable) {
        return packageInfoService.getSoftDeletedPackages(pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Khôi phục gói thuê dựa trên id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói thuê."),
            @ApiResponse(responseCode = "200", description = "Khôi phục gói thuê thành công.")
    })
    @PatchMapping("/package/restore/{id}")
    public ResponseEntity<BaseResponse> restoreById(@PathVariable String id) {
        return packageInfoService.restoreById(id);
    }

    @Operation(summary = "Lấy danh sách gói thuê đang active, có phân trang.", description = "Roles: tất cả")
    @ApiResponse(responseCode = "200", description = "Thành công.")
    @GetMapping("/auth/package/getAllActive")
    public ResponseEntity<BaseResponse> getAllActive(@ParameterObject Pageable pageable) {
        boolean isActive = true;
        return packageInfoService.getAllActiveOrInactive(pageable, isActive);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách gói thuê đang inactive, có phân trang.", description = "Roles: trừ user")
    @ApiResponse(responseCode = "200", description = "Thành công.")
    @GetMapping("/package/getAllInactive")
    public ResponseEntity<BaseResponse> getAllInactive(@ParameterObject Pageable pageable) {
        boolean isActive = false;
        return packageInfoService.getAllActiveOrInactive(pageable, isActive);
    }

    @Operation(summary = "Tìm kiếm gói thuê theo tên, có phân trang.",
            description = "Điều kiện tìm: packageName:containingIgnoreCase + isActive=true + isDeleted=false.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói thuê nào."),
            @ApiResponse(responseCode = "200", description = "Thành công.")
    })
    @GetMapping("/auth/package/search")
    public ResponseEntity<BaseResponse> searchByNameContaining(@ParameterObject Pageable pageable,
                                                               @RequestParam String input) {
        return packageInfoService.findByNameContaining(pageable, input);
    }

}
