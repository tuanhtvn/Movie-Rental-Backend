package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.BannerCreationDTO;
import com.rental.movie.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách tất cả banner.")
    @ApiResponse(responseCode = "200", description = "OK.")
    @GetMapping("/getAll")
    public ResponseEntity<BaseResponse> getAllBanners() {
        return bannerService.getAllBanners();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Tạo một banner mới", description = "Yêu cầu đầu vào: imageUrl (String), idFilm (String)")
    @ApiResponse(responseCode = "201", description = "Tạo banner thành công.")
    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createBanner(@RequestBody @Valid BannerCreationDTO bannerCreationDTO) {
        return bannerService.createBanner(bannerCreationDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Cập nhật banner dựa trên id.", description = "Chỉ cập nhật 3 trường: imageUrl (String), idFilm (String), isActive (boolean)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy banner!"),
            @ApiResponse(responseCode = "200", description = "Cập nhật banner thành công.")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse> updateById(@PathVariable String id,
                                                   @RequestBody @Valid BannerCreationDTO newBanner) {
        return bannerService.updateBanner(id, newBanner);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Xoá mềm banner dựa trên id.", description = "Cập nhật isDeleted = true")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy banner!"),
            @ApiResponse(responseCode = "200", description = "Xoá mềm banner thành công.")
    })
    @PatchMapping("/softDelete/{id}")
    public ResponseEntity<BaseResponse> softDeleteById(@PathVariable String id) {
        return bannerService.softDeleteById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Tìm banner dựa trên filmName (nhập chính xác) hoặc idFilm.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy banner!"),
            @ApiResponse(responseCode = "200", description = "Tìm thấy banner.")
    })
    @GetMapping("/findBanner")
    public ResponseEntity<BaseResponse> findBannerByFilmName_orByFilmId(@RequestParam String input) {
        return bannerService.findByFilmNameOrFilmId(input);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách banner đã bị xoá mềm.")
    @ApiResponse(responseCode = "200", description = "OK.")
    @GetMapping("/getSoftDeletedBanners")
    public ResponseEntity<BaseResponse> getSoftDeletedBanners() {
        return bannerService.getSoftDeletedBanners();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Khôi phục banner đã bị xoá mềm dựa trên id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy banner!"),
            @ApiResponse(responseCode = "200", description = "Khôi phục thành công.")
    })
    @PutMapping("/restore/{id}")
    public ResponseEntity<BaseResponse> restoreById(@PathVariable String id) {
        return bannerService.restoreBannerById(id);
    }

}
