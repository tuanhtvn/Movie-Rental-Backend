package com.rental.movie.models.entity;

import java.time.ZonedDateTime;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalPackage {
    private ZonedDateTime registrationDate;
    private ZonedDateTime expirationDate;
    private Boolean isRenewal = true;
    @DBRef
    private PackageInfo packageInfo;
}
