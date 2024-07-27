package com.rental.movie.model.dto;

import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class SupportRequestDTO {
    private String title;
    private String content;
    private ZonedDateTime requestTime;
    private Boolean isResolved;
    private String userId;
}