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
    private String id; // khóa chính
    private String title; // tiêu đề
    private String content; // nội dung
    private ZonedDateTime requestTime; // thời gian gửi
    private Boolean isResolved = false; // trạng thái đã giải quyết
    @DBRef
    private User user; // người gửi
}
