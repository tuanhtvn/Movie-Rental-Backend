package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO {
    @JsonProperty("IdUser")
    private String idUser;
    @JsonProperty("Role")
    private String role;
    @JsonProperty("FullName")
    private String fullName;
    @JsonProperty("Avatar")
    private String avatar;
    @JsonProperty("Token")
    private String token;
    @JsonProperty("ExpiredAt")
    private String expiredAt;
}