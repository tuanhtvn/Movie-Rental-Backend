package com.rental.movie.model.dto;

import com.rental.movie.common.PaymentStatus;
import com.rental.movie.model.entity.PackageInfo;
import com.rental.movie.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDTO {
    private String id;
    private ZonedDateTime issueDate;
    private List<FilmResponseDTO> films;
    private PackageInfo packageInfo;
    private Double totalPrice;
    private PaymentStatus paymentStatus;
    private String userId;
}
