package com.rental.movie.models.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String id;
    private String text;
    @DBRef
    private User user;
    @CreatedDate
    private Instant createdAt;

    public ZonedDateTime getCreatedAt() {
        return createdAt.atZone(ZoneId.systemDefault());
    }

}
