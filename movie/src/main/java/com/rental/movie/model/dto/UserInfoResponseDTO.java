package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponseDTO {
    @JsonProperty("FullName")
    private String fullName;
    @JsonProperty("Avatar")
    private String avatar;
    @JsonProperty("Email")
    private String email;
}
