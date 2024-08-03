package com.rental.movie.model.entity;

import java.time.ZonedDateTime;


import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PaymentInfo")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo extends BaseEntity{
    @Id
    private String id; // khóa 
    private String bankName; // tên ngân hàng
    private String cardNumber; // số thẻ
    private String cardHolderName; // tên chủ thẻ
    private ZonedDateTime issueDate; // ngày phát hành
}
