package com.rental.movie.models.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo {
    @Id
    private String id;
    private String bankName;
    private String cardNumber;
    private String cardHolderName;
    private ZonedDateTime issueDate;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    public ZonedDateTime getCreatedAt() {
        return createdAt.atZone(ZoneId.systemDefault());
    }

    public ZonedDateTime getUpdatedAt() {
        return createdAt.atZone(ZoneId.systemDefault());
    }
}
