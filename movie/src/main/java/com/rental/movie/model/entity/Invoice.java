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
    private String id; // khóa chính
    private ZonedDateTime issueDate; // ngày tạo hóa đơn
    private PaymentStatus paymentStatus; // trạng thái thanh toán
    private Double totalPrice; // tổng giá
    @DBRef
    private List<Film> films = new ArrayList<>(); // danh sách phim thuê
    @DBRef
    private PackageInfo packageInfo; // thông tin gói thuê
    @DBRef
    private User user; // người thuê
}
