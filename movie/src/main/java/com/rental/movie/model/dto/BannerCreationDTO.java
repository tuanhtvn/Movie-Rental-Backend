package com.rental.movie.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerCreationDTO {

    //This 2 fields is required
    @Schema(description = "Đường dẫn ảnh của banner.", example = "https://example66.com/image.jpg")
    @NotBlank(message = "Bạn chưa nhập đường dẫn ảnh!")
    private String imageUrl;

    @Schema(description = "ID phim của banner.", example = "66911ea4371ec16ae7a05629")
    @NotBlank(message = "Bạn chưa nhập idFilm!")
    private String idFilm;

    //created automatically
    private Boolean isActive = true;
    private Boolean isDeleted = false;
}
