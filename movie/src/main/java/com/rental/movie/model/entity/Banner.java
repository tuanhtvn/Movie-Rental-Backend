package com.rental.movie.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Banner extends BaseEntity {
    @Id
    private String id; // khóa chính
    private String imageUrl; // đường dẫn ảnh banner
    @DBRef(lazy=true)
    private Film film; // phim
}
