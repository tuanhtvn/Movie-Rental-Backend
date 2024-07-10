package com.rental.movie.models.entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;

import com.rental.movie.common.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory {
    @Id
    private String id;
    private ZonedDateTime purchaseDate;
    private Double totalPrice;
    private TransactionStatus transactionStatus;
}
