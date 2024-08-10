package com.rental.movie.model.entity;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment{
    @Id
    private String id; // khóa chính
    private String text; // nội dung
    @DBRef(lazy=true)
    private String idUser; // người bình luận
    @CreatedDate
    private Instant createdAt; // thời gian bình luận
    private String filmId; // Id của phim đã bình luận
    public String getCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return createdAt.atZone(ZoneId.systemDefault()).format(formatter);
    }

}
