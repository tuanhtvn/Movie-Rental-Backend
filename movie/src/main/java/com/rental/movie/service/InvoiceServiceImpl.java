package com.rental.movie.service;

import com.rental.movie.common.PaymentStatus;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.InvoiceResponseDTO;
import com.rental.movie.model.dto.PaymentInfoResponseDTO;
import com.rental.movie.model.entity.*;
import com.rental.movie.repository.InvoiceRepository;
import com.rental.movie.util.mapper.InvoiceMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceMapper invoiceMapper;

    @Override
    public List<InvoiceResponseDTO> getAll() {
        log.info("Get all Invoices ");
        return invoiceRepository.findAll().stream()
                .map(invoice -> invoiceMapper.convertToDTO(invoice))
                .toList();
    }

    @Override
    public InvoiceResponseDTO get(String invoiceId) {
        log.info("Get invoice id: {} ",invoiceId);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new CustomException("Không tìm thấy hóa đơn", HttpStatus.NOT_FOUND.value()));
        return invoiceMapper.convertToDTO(invoice);
    }

    @Override
    public void createInvoice(ZonedDateTime issueDate, PaymentStatus paymentStatus, Double totalPrice, List<Film> films, PackageInfo packageInfo, User user) {
        log.info("Create invoice");
        Invoice invoice = new Invoice(new ObjectId().toString(), issueDate, paymentStatus, totalPrice, films, packageInfo, user);
        invoiceRepository.save(invoice);
        log.info("Create invoice successfully");
    }
}
