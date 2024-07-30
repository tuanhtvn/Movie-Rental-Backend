package com.rental.movie.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
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
