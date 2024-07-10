package com.rental.movie.models.entity;

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
    private String id;
    private String token;
    private ZonedDateTime expireAt;
    private String info;
    private ZonedDateTime lastLoggedIn;
}
