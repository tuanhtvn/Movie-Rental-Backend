package com.rental.movie.service;

import com.rental.movie.common.PaymentStatus;
import com.rental.movie.model.dto.InvoiceResponseDTO;
import com.rental.movie.model.entity.Film;
import com.rental.movie.model.entity.PackageInfo;
import com.rental.movie.model.entity.User;

import java.time.ZonedDateTime;
import java.util.List;

public interface InvoiceService {
    public List<InvoiceResponseDTO> getAll();
    public InvoiceResponseDTO get(String invoiceId);
    public void createInvoice(ZonedDateTime issueDate, PaymentStatus paymentStatus, Double totalPrice, List<Film> films, PackageInfo packageInfo, User user);
}
