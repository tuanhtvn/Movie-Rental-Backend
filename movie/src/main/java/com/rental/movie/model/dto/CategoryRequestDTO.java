package com.rental.movie.model.dto;

import com.rental.movie.model.entity.Album;
import com.rental.movie.model.entity.Banner;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO extends BaseRequestDTO {
    @NotBlank(message = "Chưa có tên danh mục")
    @Size(max = 255, message = "Tên danh mục tên ít hơn 255 ký tự")
    private String categoryName; // tên danh mục
    private List<String> bannersId = new ArrayList<>(); // danh sách banner
    private List<String> albumsId = new ArrayList<>(); // danh sách album
}
