package com.rental.movie.model.entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "SupportRequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportRequest {
    @Id
    private String id;
    private String title;
    private String content;
    private ZonedDateTime requestTime;
    private Boolean isResolved = false;
    @DBRef
    private User user;
}
