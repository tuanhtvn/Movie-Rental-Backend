package com.rental.movie.model.entity;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Verify {
    private String code; // mã xác thực
    private ZonedDateTime expiredAt; // thời gian hết hạn
}
