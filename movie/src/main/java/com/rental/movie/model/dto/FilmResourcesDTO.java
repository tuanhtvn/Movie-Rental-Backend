package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

import com.rental.movie.common.RentalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilmResourcesDTO {
    @JsonProperty("FilmUrl")
    private String filmUrl;
    private List<SubtitleResponseDTO> subtitles = new ArrayList<>();
    private List<NarrationResponseDTO> narrations = new ArrayList<>();

}
