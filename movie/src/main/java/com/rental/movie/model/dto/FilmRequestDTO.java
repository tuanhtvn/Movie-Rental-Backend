package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.rental.movie.common.RentalType;
import com.rental.movie.model.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilmRequestDTO {
    @NotBlank(message = "Vui lòng nhập tên phim")
    @Size(max = 255, message = "Tên phim không được vượt quá 255 ký tự")
    @Schema(description = "Tên phim", example = "Bố Già")
    @JsonProperty("FilmName")
    private String filmName;

    @Schema(description = "Mô tả phim", example = "BỐ GIÀ là một bộ phim Web drama tình cảm gia đình, một dự án phim hài Tết 2020 của Trấn Thành.")
    @JsonProperty("Description")
    private String description;

    @NotBlank(message = "Vui lòng chọn đường dẫn của phim")
    @Schema(description = "Đường dẫn phim", example = "https://example.com/film.mp4")
    @JsonProperty("FilmUrl")
    private String filmUrl;

    @NotBlank(message = "Vui lòng chọn ảnh Thumbnail")
    @Schema(description = "Đường dẫn ảnh Thumbnail", example = "https://example.com/thumbnail.jpg")
    @JsonProperty("ThumbnailUrl")
    private String thumbnailUrl;

    @Schema(description = "Đường dẫn Trailer", example = "https://example.com/trailer.mp4")
    @JsonProperty("TrailerUrl")
    private String trailerUrl;

    @Schema(description = "Ngày phát hành", example = "01/01/2022")
    @JsonProperty("ReleaseDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private String releaseDate;

    @Schema(description = "Thời lượng phim", example = "120")
    @JsonProperty("Duration")
    private String duration;

    @Schema(description = "Diễn viên", example = "Trấn Thành")
    @JsonProperty("Actors")
    private List<String> actors;

    @Schema(description = "Đạo diễn", example = "Trấn Thành")
    @JsonProperty("Director")
    private String director;

    @Schema(description = "Ngôn ngữ", example = "Vietnamese")
    @JsonProperty("Language")
    private String language;

    @Schema(description = "Độ tuổi", example = "13")
    @JsonProperty("Age")
    private Integer age;

    @Schema(description = "Giá", example = "20000")
    @JsonProperty("Price")
    private Double price;

    @Schema(description = "Thời hạn giới hạn", example = "100")
    @JsonProperty("LimitTime")
    private Integer limitTime;

    @Schema(description = "Loại thuê phim", example = "RENTAL")
    @JsonProperty("RentalType")
    private RentalType rentalType;

    @Schema(description = "Danh sách các phụ đề")
    @JsonProperty("SubtitlesId")
    private List<String> subtitlesId;

    @Schema(description = "Danh sách các thuyết minh")
    @JsonProperty("NarrationsId")
    private List<String> narrationsId;

    @Schema(description = "Danh sách các thể loại")
    @JsonProperty("GenresId")
    private List<String> genresId;

}
