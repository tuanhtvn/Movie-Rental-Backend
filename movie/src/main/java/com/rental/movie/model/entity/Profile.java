package com.rental.movie.model.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    private String id; // khóa chính
    private String profileName; // tên profile
    private String imageUrl; // ảnh đại diện
    private Boolean isChild; // trạng thái trẻ em
    private Boolean isDefault; // trạng thái mặc định

    @DBRef(lazy = true)
    private List<Film> selectedMovies = new ArrayList<>();

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public ZonedDateTime getCreatedAt() {
        return createdAt.atZone(ZoneId.systemDefault());
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt.atZone(ZoneId.systemDefault());
    }
}
