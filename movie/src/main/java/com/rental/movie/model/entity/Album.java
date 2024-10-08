package com.rental.movie.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Album")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Album extends BaseEntity {
    @Id
    private String id; // khóa chính
    private String albumName; // tên album
    @DBRef(lazy=true)
    private List<Film> film = new ArrayList<>();; // danh sách phim
}
