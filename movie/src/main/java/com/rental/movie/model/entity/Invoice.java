package com.rental.movie.model.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.rental.movie.common.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    private String id;
    private ZonedDateTime issueDate;
    private PaymentStatus paymentStatus;
    private Double totalPrice;
    @DBRef
    private List<Film> films = new ArrayList<>();
    @DBRef
    private PackageInfo packageInfo;
    @DBRef
    private User user;
}
