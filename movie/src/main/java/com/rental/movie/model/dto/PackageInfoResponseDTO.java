package com.rental.movie.model.dto;

import com.rental.movie.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PackageInfoResponseDTO extends BaseEntity {
    private String id; // khóa chính
    private String packageName; // tên gói
    private String description; // mô tả
    private Double price; // giá
    private Integer timeDuration; // thời gian sử dụng
}
