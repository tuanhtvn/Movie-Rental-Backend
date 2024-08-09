package com.rental.movie.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Subtitle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subtitle {
    @Id
    private String id; // khóa chính
    private String subtitleName; // tên phụ đề
    private String subtitleUrl; // đường dẫn phụ đề
}
