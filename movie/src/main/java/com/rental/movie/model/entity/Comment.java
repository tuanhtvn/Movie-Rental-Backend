package com.rental.movie.model.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity{
    @Id
    private String id; // khóa chính
    private String text; // nội dung
    @DBRef(lazy=true)
    private String idUser; // người bình luận
    @CreatedDate
    //private Instant createdAt; // thời gian bình luận
    private String filmId; // Id của phim đã bình luận
    //public ZonedDateTime getCreatedAt() {
    //    return createdAt.atZone(ZoneId.systemDefault());
    //}

}
