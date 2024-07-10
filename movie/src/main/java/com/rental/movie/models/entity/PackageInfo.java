package com.rental.movie.models.entity;

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
    private String id;
    private String packageName;
    private String description;
    private Double price;
    private Integer timeDuration;
}
