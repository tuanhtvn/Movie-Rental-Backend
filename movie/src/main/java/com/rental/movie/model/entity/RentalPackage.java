package com.rental.movie.model.entity;

import java.time.Duration;
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
    @DBRef(lazy=true)
    private PackageInfo packageInfo; // thông tin gói thuê

    //hàm tính thời gian còn lại của gói thuê (đơn vị: phút):
    public long getMinutesLeft() {
        Duration duration = Duration.between(ZonedDateTime.now(), this.expirationDate);
        return duration.toMinutes() > 0 ? duration.toMinutes() : 0;
    }
}
