package com.rental.movie.model.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subtitle {
    @Id
    private String id; // khóa chính
    private String subtitleName; // tên phụ đề
    private String subtitleUrl; // đường dẫn phụ đề
}
