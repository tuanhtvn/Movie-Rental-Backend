package com.rental.movie.model.entity;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Verify {
    private String code;
    private ZonedDateTime expiredAt;
}
