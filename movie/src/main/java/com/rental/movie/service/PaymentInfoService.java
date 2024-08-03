package com.rental.movie.service;

import com.rental.movie.model.dto.PaymentInfoRepuestDTO;
import com.rental.movie.model.dto.PaymentInfoResponseDTO;

import java.util.List;

public interface PaymentInfoService {
    public List<PaymentInfoResponseDTO> getAll();
    public PaymentInfoResponseDTO get(String paymentInfoId);
    public List<PaymentInfoResponseDTO> getAllSoftDelete(); //isDeleted=true
    public List<PaymentInfoResponseDTO> getAllActive(); //isActive=true && isDeleted=false
    public List<PaymentInfoResponseDTO> getAllInActive(); //isActive=false && isDeleted=false
    public PaymentInfoResponseDTO addPaymentInfo(PaymentInfoRepuestDTO paymentInfoRepuestDTO);
    public PaymentInfoResponseDTO update(String paymentInfoId,PaymentInfoRepuestDTO paymentInfoRepuestDTO);
    public void softDeletePaymentInfo(String paymentInfoId);
    public void restorePaymentInfo(String paymentInfoId);
    public void activatePaymentInfo(String paymentInfoId);
    public void deAvtivatePaymentInfo(String paymentInfoId);
}
