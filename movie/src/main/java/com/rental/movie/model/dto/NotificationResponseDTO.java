package com.rental.movie.model.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Content")
    private String content;
    @JsonProperty("SentTime")
    private ZonedDateTime sentTime;
    @JsonProperty("IsRead")
    private Boolean isRead = false;
    @JsonProperty("UserId")
    private String userId;
}