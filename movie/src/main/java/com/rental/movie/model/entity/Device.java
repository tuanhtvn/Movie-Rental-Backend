package com.rental.movie.model.entity;

import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    @Id
    private String id; // khóa chính
    private String token; // token
    private ZonedDateTime expireAt; // thời gian hết hạn
    private String info; // thông tin thiết bị
    private String ip; // địa chỉ IP
    private ZonedDateTime lastLoggedIn; // thời gian đăng nhập cuối cùng

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Device device = (Device) obj;
        return Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
