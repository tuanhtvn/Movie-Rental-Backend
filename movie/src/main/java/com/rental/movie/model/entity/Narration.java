package com.rental.movie.model.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Narration {
    @Id
    private String id; // khóa chính
    private String language; // ngôn ngữ
    private String narrationUrl; // đường dẫn thuyết minh
}
