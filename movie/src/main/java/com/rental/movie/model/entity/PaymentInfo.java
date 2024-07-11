package com.rental.movie.model.entity;

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
    private String id; // khóa 
    private String bankName; // tên ngân hàng
    private String cardNumber; // số thẻ
    private String cardHolderName; // tên chủ thẻ
    private ZonedDateTime issueDate; // ngày phát hành

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
