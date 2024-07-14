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

@Document(collection = "Category")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    @Id
    private String id; // khóa chính
    private String categoryName; // tên danh mục
    @DBRef(lazy=true)
    private List<Banner> banners = new ArrayList<>(); // danh sách banner
    @DBRef(lazy=true)
    private List<Album> albums = new ArrayList<>(); // danh sách album
}
