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
public class GenreResponseDTO extends BaseResponseDTO {
    private String genreName; // tên thể loại
    private List<FilmResponseDTO> filmsId = new ArrayList<>(); // danh sách phim
}
