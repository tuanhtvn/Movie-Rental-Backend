package com.rental.movie.model.dto;

import lombok.Data;

import java.time.ZonedDateTime;
@Data
public class PaymentInfoResponseDTO extends BaseResponseDTO{
    private String id;
    private String bankName;
    private String cardNumber;
    private String cardHolderName;
    private ZonedDateTime issueDate;
}
