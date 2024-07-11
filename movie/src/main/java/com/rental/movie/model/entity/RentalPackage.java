package com.rental.movie.model.entity;

import java.time.ZonedDateTime;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalPackage {
    private ZonedDateTime registrationDate; // ngày đăng ký
    private ZonedDateTime expirationDate; // ngày hết hạn
    private Boolean isRenewal = true; // trạng thái gia hạn
    @DBRef
    private PackageInfo packageInfo; // thông tin gói thuê
}
