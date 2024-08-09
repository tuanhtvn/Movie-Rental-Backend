package com.rental.movie.model.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

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
    private String Id; // khóa chính
    private String FilmName; // tên phim
    private String FilmUrl; // đường dẫn phim
    private String Description; // mô tả
    private String ThumbnailUrl; // đường dẫn ảnh thumbnail
    private String TrailerUrl; // đường dẫn trailer
    private String ReleaseDate; // ngày phát hành
    private String Duration; // thời lượng
    private List<String> Actors; // danh sách diễn viên
    private String Director; // đạo diễn
    private String Language; // ngôn ngữ
    private Integer NumberOfViews = 0; // số lượt xem
    private Map<String, Integer> Ratings = new HashMap<>(); // lưu trữ đánh giá của người dùng
    private Integer Age; // độ tuổi
    private RentalType RentalType; // loại thuê phim
    private Double Price; // giá
    private Integer LimitTime; // thời hạn xem
    @DBRef(lazy = true)
    private List<Subtitle> subtitles = new ArrayList<>(); // danh sách phụ đề
    @DBRef(lazy = true)
    private List<Narration> narrations = new ArrayList<>(); // danh sách thuyết minh
    @DBRef(lazy = true)
    private List<String> commentsId = new ArrayList<>(); // danh sách bình luận
    @DBRef(lazy = true)
    private List<String> genresId = new ArrayList<>(); // danh sách thể loại

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Film film = (Film) obj;
        return Objects.equals(Id, film.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }
}
