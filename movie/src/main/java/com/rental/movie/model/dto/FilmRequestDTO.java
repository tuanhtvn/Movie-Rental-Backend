package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FilmRequestDTO {
    @NotBlank(message = "Vui lòng nhập tên phim")
    @Size(max = 255, message = "Tên phim không được vượt quá 255 ký tự")
    @Schema(description = "Tên phim", example = "Bố Già")
    @JsonProperty("FilmName")
    private String filmName;
    @NotBlank(message = "Vui lòng chọn phim")
    @Schema(description = "Đường dẫn của phim", example = "https://drive.google.com/file/d/1AUhDl9Mqfab3qL89-iOmoTKa-Z7kYHXU/view?usp=sharing")
    @JsonProperty("FilmUrl")
    private String filmlUrl;
    @NotBlank(message = "Vui lòng chọn ảnh Thumbnail")
    @Schema(description = "Đường dẫn ảnh Thumbnail", example = "https://example.com/thumbnail.jpg")
    @JsonProperty("ThumbnailUrl")
    private String thumbnailUrl;
    @NotBlank(message = "Vui lòng nhập mô tả phim")
    @Schema(description = "Mô tả phim", example = "BỐ GIÀ là một bộ phim Web drama tình cảm gia đình, một dự án phim hài Tết 2020 của Trấn Thành. " +
            "Trong phim, Trấn Thành đóng vai chính - một ông bố tính cục súc, bảo thủ nhưng rất thương con, luôn quan tâm gia đình. " +
            "Phim xoay quanh đề tài thế giới giang hồ, xoáy vào chuyện giữ bản chất lương thiện hay chạy theo tiền bạc.")
    @JsonProperty("Description")
    private String description;
}
