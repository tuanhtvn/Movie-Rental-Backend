package com.rental.movie.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Banner")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Banner entity")
public class Banner extends BaseEntity {
    @Id
    @Schema(description = "Unique identifier of Banner")
    private String id; // khóa chính

    @Schema(description = "Banner image url", example = "https://example66.com/image.jpg")
    private String imageUrl; // đường dẫn ảnh banner

    @DBRef(lazy=true)
    @Schema(description = "This banner is from this film")
    private Film film; // phim
}
