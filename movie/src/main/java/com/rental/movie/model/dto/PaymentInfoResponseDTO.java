package com.rental.movie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoResponseDTO extends BaseResponseDTO{
    private String id;
    private String bankName;
    private String cardNumber;
    private String cardHolderName;
    private ZonedDateTime issueDate;
}
