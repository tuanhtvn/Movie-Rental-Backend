package com.rental.movie.model.dto;

import com.rental.movie.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerResponseDTO extends BaseEntity {
    private String id;
    private String imageUrl;
    private String filmId; // khóa chính phim
    private String filmName; // tên phim
}
