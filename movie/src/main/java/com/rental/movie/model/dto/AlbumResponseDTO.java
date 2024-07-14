package com.rental.movie.model.dto;

import com.rental.movie.model.entity.Film;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResponseDTO extends BaseResponseDTO {
    private String albumName; // tên album
    private List<Film> film; // danh sách phim * Chưa có film DTO nên tạm để Film
}
