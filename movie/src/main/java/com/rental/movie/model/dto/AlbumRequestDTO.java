package com.rental.movie.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class AlbumRequestDTO extends BaseRequestDTO {
    @NotBlank(message = "Chưa có tên Album")
    @Size(max = 255, message = "Tên Album nên ít hơn 255 ký tự")
    private String albumName; // tên album
    private List<String> filmId = new ArrayList<>(); // danh sách phim
}
