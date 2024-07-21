package com.rental.movie.model.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyResponseDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("ExpiredAt")
    private ZonedDateTime expiredAt;
}
