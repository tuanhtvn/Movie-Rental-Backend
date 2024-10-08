package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.service.RentalManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/rental-management")
@Slf4j
public class RentalManagementController {
    @Autowired
    private RentalManagementService rentalManagementService;

    //gói thuê
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @Operation(summary = "Lấy gói thuê dựa trên userId.",
            description = "Return: thông tin gói thuê, minutesLeft (số phút sử dụng còn lại).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng hoặc gói thuê."),
            @ApiResponse(responseCode = "200", description = "Thành công.")
    })
    @GetMapping("/getRentalPackage/{userId}")
    public ResponseEntity<BaseResponse> getRentalPackageByUserId(@PathVariable String userId, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.info("Lấy gói thuê dựa trên userId, vai trò của người dùng hiện tại: " + authorities);

        boolean isManager = authorities.stream().anyMatch(
                role -> role.getAuthority().equals("ROLE_ADMIN") || role.getAuthority().equals("ROLE_EMPLOYEE")
        );
        //Nếu không phải là người quản trị hoặc userId không trùng với người dùng hiện tại
        if (!isManager && !userId.equals(authentication.getName())){
            throw new AccessDeniedException("Bạn không có quyền truy cập vào tài nguyên này!");
        }

        return rentalManagementService.getRentalPackageByUserId(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Huỷ gia hạn tự động cho gói thuê.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng hoặc gói thuê."),
            @ApiResponse(responseCode = "200", description = "Thành công.")
    })
    @PutMapping("/disableAutoRenewal")
    public ResponseEntity<BaseResponse> disableAutoRenewal() {
        boolean enabled = false;
        return rentalManagementService.setAutoRenewal(enabled);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Bật gia hạn tự động cho gói thuê.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng hoặc gói thuê."),
            @ApiResponse(responseCode = "200", description = "Thành công.")
    })
    @PutMapping("/enableAutoRenewal")
    public ResponseEntity<BaseResponse> enableAutoRenewal() {
        boolean enabled = true;
        return rentalManagementService.setAutoRenewal(enabled);
    }

    //phim thuê
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @Operation(summary = "Lấy danh sách phim thuê theo userId, sắp xếp theo thứ tự tăng dần của minutesLeft.",
            description = "Trả về danh sách phim thuê kèm theo thời gian sử dụng còn lại (minutesLeft) của từng phim thuê.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng hoặc phim thuê."),
            @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách phim thuê.")
    })
    @GetMapping("/getRentedFilms/{userId}")
    public ResponseEntity<BaseResponse> getRentedFilms(@PathVariable String userId, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.info("Lấy ds phim thuê dựa trên userId, vai trò của người dùng hiện tại: " + authorities);

        boolean isManager = authorities.stream().anyMatch(
                role -> role.getAuthority().equals("ROLE_ADMIN") || role.getAuthority().equals("ROLE_EMPLOYEE")
        );
        //Nếu không phải là người quản trị hoặc userId không trùng với người dùng hiện tại
        if (!isManager && !userId.equals(authentication.getName())){
            throw new AccessDeniedException("Bạn không có quyền truy cập vào tài nguyên này!");
        }

        return rentalManagementService.getRentedFilmsByUserId(userId);
    }

    //click vào xem phim thì gọi
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Kích hoạt tính giờ phim thuê.", description = "Gọi khi user click vào xem phim.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng hoặc phim thuê."),
            @ApiResponse(responseCode = "400", description = "Phim thuê này đã được kích hoạt trước đó rồi!"),
            @ApiResponse(responseCode = "200", description = "Đã kích hoạt tính giờ phim thuê."),
    })
    @PutMapping("/startTimming/{rentedFilmId}")
    public ResponseEntity<BaseResponse> startTimmingRentedFilmById(@PathVariable String rentedFilmId) {
        return rentalManagementService.startTimmingRentedFilm(rentedFilmId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Lấy phim thuê của người dùng hiện tại (dựa trên rentedFilmId).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng hoặc phim thuê."),
            @ApiResponse(responseCode = "200", description = "Tìm phim thuê thành công."),
    })
    @GetMapping("/getRentedFilmById/{rentedFilmId}")
    public ResponseEntity<BaseResponse> getRentedFilmById(@PathVariable String rentedFilmId) {
        return rentalManagementService.getRentedFilmById(rentedFilmId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Tìm phim thuê của người dùng hiện tại (dựa trên tên phim).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng hoặc phim thuê."),
            @ApiResponse(responseCode = "200", description = "Tìm phim thuê thành công."),
    })
    @GetMapping("/getRentedFilmByFilmName")
    public ResponseEntity<BaseResponse> getRentedFilmByFilmName(@RequestParam String filmName) {
        return rentalManagementService.getRentedFilmByFilmName(filmName);
    }

}
