package com.rental.movie.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document(collection = "Genre")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Genre extends BaseEntity {
    @Id
    private String id; // khóa chính
    private String genreName; // tên thể loại

    @DBRef
    private List<Film> films = new ArrayList<>(); // danh sách phim
}
