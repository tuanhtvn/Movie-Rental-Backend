package com.rental.movie.model.dto;

import com.rental.movie.model.entity.Film;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class GenreRequestDTO extends BaseRequestDTO {
    @NotBlank(message = "Chưa có tên thể loại")
    @Size(max = 255, message = "Tên thể loại nên ít hơn 255 ký tự")
    private String genreName; // tên thể loại
    private List<String> filmsId = new ArrayList<>(); // danh sách phim
}
