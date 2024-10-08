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
public class AlbumResponseDTO extends BaseResponseDTO {
    private String albumName; // tên album
    private List<FilmResponseDTO> film = new ArrayList<>(); // danh sách phim * Chưa có film DTO nên tạm để Film
}
