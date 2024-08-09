package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NarrationResponseDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Language")
    private String language;
    @JsonProperty("NarrationUrl")
    private String narrationUrl;
}
