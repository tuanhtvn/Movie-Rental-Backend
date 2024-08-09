package com.rental.movie.common;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenResponse {
    private String token;
    private Instant expiredAt;

    public String getExpiredAt() {
        return this.expiredAt.getEpochSecond() + "";
    }
}
