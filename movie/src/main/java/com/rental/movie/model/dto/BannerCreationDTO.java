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
    @Schema(description = "Banner image URL", example = "https://example66.com/image.jpg")
    @NotBlank(message = "Image url is mandatory!")
    private String imageUrl;

    @Schema(description = "ID of the film associated with the banner", example = "66911ea4371ec16ae7a05629")
    @NotBlank(message = "idFilm is mandatory!")
    private String idFilm;

    //created automatically
    private Boolean isActive = true;
    private Boolean isDeleted = false;
}
