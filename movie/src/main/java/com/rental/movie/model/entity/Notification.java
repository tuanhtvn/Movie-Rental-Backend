package com.rental.movie.model.entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    private String id; // khóa chính
    private String title; // tiêu đề
    private String content; // nội dung
    private ZonedDateTime sentTime; // thời gian gửi
    private Boolean isRead = false; // trạng thái đã đọc
    @DBRef
    private User user; // người nhận
}
