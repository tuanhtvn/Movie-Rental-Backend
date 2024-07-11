package com.rental.movie.model.entity;

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
    private String id; // khóa chính
    private ZonedDateTime purchaseDate; // thời gian mua
    private Double totalPrice; // tổng giá
    private TransactionStatus transactionStatus; // trạng thái giao dịch
}
