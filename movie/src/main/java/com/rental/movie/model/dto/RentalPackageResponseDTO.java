package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rental.movie.model.entity.PackageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalPackageResponseDTO {
    private ZonedDateTime registrationDate; // ngày đăng ký
    private ZonedDateTime expirationDate; // ngày hết hạn
    private Boolean isRenewal = true; // trạng thái gia hạn
    private PackageInfo packageInfo; // thông tin gói thuê
    private long minutesLeft; //số phút còn lại
//    private boolean justRenewedAuto = false; //kiểm tra xem có vừa được gia hạn tự động không
}
