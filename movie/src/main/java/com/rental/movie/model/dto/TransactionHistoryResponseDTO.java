package com.rental.movie.model.dto;

import com.rental.movie.common.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryResponseDTO {
    private String id;
    private ZonedDateTime purchaseDate;
    private Double totalPrice; // tổng giá
    private TransactionStatus transactionStatus; // trạng thái giao dịch
}
