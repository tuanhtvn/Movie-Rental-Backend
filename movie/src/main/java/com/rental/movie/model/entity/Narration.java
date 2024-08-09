package com.rental.movie.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Narration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Narration {
    @Id
    private String id; // khóa chính
    private String language; // ngôn ngữ
    private String narrationUrl; // đường dẫn thuyết minh
}
