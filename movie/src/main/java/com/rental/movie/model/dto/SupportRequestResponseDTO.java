package com.rental.movie.model.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SupportRequestResponseDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Content")
    private String content;
    @JsonProperty("RequestTime")
    private ZonedDateTime requestTime;
    @JsonProperty("IsResolved")
    private Boolean isResolved;
    @JsonProperty("UserId")
    private String userId;
}