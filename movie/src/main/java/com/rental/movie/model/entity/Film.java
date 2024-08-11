package com.rental.movie.model.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonProperty("FilmName")
    private String filmName; // tên phim
    @JsonProperty("FilmUrl")
    private String filmUrl; // đường dẫn phim
    @JsonProperty("Description")
    private String description; // mô tả
    @JsonProperty("ThumbnailUrl")
    private String thumbnailUrl; // đường dẫn ảnh thumbnail
    @JsonProperty("TrailerUrl")
    private String trailerUrl; // đường dẫn trailer
    @JsonProperty("ReleaseDate")
    private String releaseDate; // ngày phát hành
    @JsonProperty("Duration")
    private String duration; // thời lượng
    @JsonProperty("Actors")
    private List<String> actors; // danh sách diễn viên
    @JsonProperty("Director")
    private String director; // đạo diễn
    @JsonProperty("Language")
    private String language; // ngôn ngữ
    @JsonProperty("NumberOfViews")
    private Integer numberOfViews; // số lượt xem
    @JsonProperty("Ratings")
    private Map<String, Integer> ratings = new HashMap<>(); // lưu trữ đánh giá của người dùng
    @JsonProperty("Age")
    private Integer age; // độ tuổi
    @JsonProperty("RentalType")
    private RentalType rentalType; // loại thuê phim
    @JsonProperty("Price")
    private Double price; // giá
    @JsonProperty("LimitTime")
    private Integer limitTime; // thời hạn xem
    @DBRef(lazy = true)
    private List<Subtitle> subtitles = new ArrayList<>(); // danh sách phụ đề
    @DBRef(lazy = true)
    private List<Narration> narrations = new ArrayList<>(); // danh sách thuyết minh
    private List<String> comments = new ArrayList<>(); // danh sách bình luận
    @DBRef(lazy = true)
    private List<String> genres = new ArrayList<>(); // danh sách thể loại

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
