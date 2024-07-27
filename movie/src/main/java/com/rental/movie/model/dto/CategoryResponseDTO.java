package com.rental.movie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO extends BaseResponseDTO {
    private String categoryName; // tên danh mục
    private List<BannerResponseDTO> banners = new ArrayList<>(); // danh sách banner
    private List<AlbumResponseDTO> albums = new ArrayList<>(); // danh sách album
}
