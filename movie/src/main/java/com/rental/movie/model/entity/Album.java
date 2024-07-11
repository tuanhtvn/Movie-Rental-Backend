package com.rental.movie.model.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Album extends BaseEntity {
    @Id
    private String id; // khóa chính
    private String albumName; // tên album
    @DBRef
    private List<Film> film; // danh sách phim
}
