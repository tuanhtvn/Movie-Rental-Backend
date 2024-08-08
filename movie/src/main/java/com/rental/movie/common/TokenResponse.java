package com.rental.movie.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenResponse {
    private String token;
    private Instant expiredAt;

    public String getExpiredAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(this.expiredAt);
    }
}
