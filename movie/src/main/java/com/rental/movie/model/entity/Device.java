package com.rental.movie.model.entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    private String id; // khóa chính
    private String token; // token
    private ZonedDateTime expireAt; // thời gian hết hạn
    private String info; // thông tin thiết bị
    private ZonedDateTime lastLoggedIn; // thời gian đăng nhập cuối cùng
}
