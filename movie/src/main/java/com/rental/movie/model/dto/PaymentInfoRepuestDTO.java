package com.rental.movie.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoRepuestDTO extends BaseRequestDTO{
    @NotBlank(message = "Tên ngân hàng không được để trống")
    private String bankName;

    @NotBlank(message = "Số thẻ không được để trống")
    private String cardNumber;

    @NotBlank(message = "Tên chủ thẻ không được để trống")
    private String cardHolderName;

    @NotNull(message = "Ngày phát hành không được để trống")
    private ZonedDateTime issueDate;
}
