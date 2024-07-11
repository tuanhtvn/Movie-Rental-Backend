package com.rental.movie.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document(collection = "PackageInfo")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PackageInfo extends BaseEntity {
    @Id
    private String id; // khóa chính
    private String packageName; // tên gói
    private String description; // mô tả
    private Double price; // giá
    private Integer timeDuration; // thời gian sử dụng
}
