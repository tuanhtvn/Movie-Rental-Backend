package com.rental.movie.model.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DeviceResponseDTO {
    @JsonProperty("Id")
    private String id; // khóa chính
    @JsonProperty("Info")
    private String info; // thông tin thiết bị
    @JsonProperty("LastLoggedIn")
    private ZonedDateTime lastLoggedIn; // thời gian đăng nhập cuối cùng
    @JsonProperty("IsCurrentDevice")
    private Boolean isCurrentDevice = false; // thiết bị hiện tại
}
