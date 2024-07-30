package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDTO extends BaseRequestDTO {
    @Schema(description = "Nội dung bình luận", example = "Phim rất hay!")
    @JsonProperty("text")
    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String text;

    @Schema(description = "Người dùng bình luận", example = "66911ea4371ec16a")
    @JsonProperty("idUser")
    @NotBlank(message = "ID người dùng không được để trống")
    private String idUser;
}
