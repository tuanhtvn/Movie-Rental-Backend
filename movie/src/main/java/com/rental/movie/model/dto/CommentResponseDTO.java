package com.rental.movie.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDTO extends BaseResponseDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Text")
    private String text;
    @JsonProperty("CreatedAt")
    private ZonedDateTime createdAt;
    @JsonProperty("IdUser")
    private String idUser;
    @JsonProperty("FilmId")
    private String filmId;
}
