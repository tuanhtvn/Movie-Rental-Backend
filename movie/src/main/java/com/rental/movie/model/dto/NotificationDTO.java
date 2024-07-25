package com.rental.movie.model.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String title;
    private String content;
    private ZonedDateTime sentTime;
    private Boolean isRead = false;
    private String userId;
}