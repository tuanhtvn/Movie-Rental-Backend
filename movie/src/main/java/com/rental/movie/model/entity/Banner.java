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
    @Schema(description = "Định danh duy nhất của banner.")
    private String id; // khóa chính

    @Schema(description = "Đường dẫn ảnh của banner.", example = "https://example66.com/image.jpg")
    private String imageUrl; // đường dẫn ảnh banner

    @DBRef(lazy=true)
    @Schema(description = "Banner này được dùng cho phim này.")
    private Film film; // phim
}
