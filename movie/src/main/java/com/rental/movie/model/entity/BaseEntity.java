package com.rental.movie.model.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
public abstract class BaseEntity {
    private Boolean isActive = true; // Trạng thái hoạt động
    private Boolean isDeleted = false; // Trạng thái đã xóa

    @CreatedDate
    private Instant createdAt; // Thời gian tạo
    @LastModifiedDate
    private Instant updatedAt; // Thời gian cập nhật

    public ZonedDateTime getCreatedAt() {
        return createdAt.atZone(ZoneId.systemDefault());
    }

    public ZonedDateTime getUpdatedAt() {
        return createdAt.atZone(ZoneId.systemDefault());
    }
}
