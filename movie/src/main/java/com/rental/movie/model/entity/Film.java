package com.rental.movie.model.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.rental.movie.common.RentalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Film")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film extends BaseEntity {
    @Id
    private String id; // khóa chính
    private String filmName; // tên phim
    private String filmUrl; // đường dẫn phim
    private String description; // mô tả
    private String thumbnailUrl; // đường dẫn ảnh thumbnail
    private String trailerUrl; // đường dẫn trailer
    private ZonedDateTime releaseDate; // ngày phát hành
    private String duration; // thời lượng
    private String actors; // danh sách diễn viên
    private String director; // đạo diễn
    private String language; // ngôn ngữ
    private Integer numberOfViews; // số lượt xem
    private Double rating; // đánh giá
    private Integer age; // độ tuổi
    private RentalType rentalType; // loại thuê phim
    private Double price; // giá
    private Integer limitTime; // thời hạn xem

    private List<Subtitle> subtitles = new ArrayList<>(); // danh sách phụ đề
    private List<Narration> narrations = new ArrayList<>(); // danh sách thuyết minh
    private List<Comment> comments = new ArrayList<>(); // danh sách bình luận
    @DBRef(lazy = true)
    private List<Genre> genres = new ArrayList<>(); // danh sách thể loại

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Film film = (Film) obj;
        return Objects.equals(id, film.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
