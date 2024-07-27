package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("ProfileName")
    private String profileName;
    @JsonProperty("ImageUrl")
    private String imageUrl;
    @JsonProperty("IsChild")
    private Boolean isChild;
}
