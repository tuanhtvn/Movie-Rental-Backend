package com.rental.movie.service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.common.PaymentStatus;
import com.rental.movie.common.RentalType;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.InvoiceResponseDTO;
import com.rental.movie.model.entity.*;
import com.rental.movie.repository.InvoiceRepository;
import com.rental.movie.util.mapper.InvoiceMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceMapper invoiceMapper;
    @Autowired
    private IAuthentication authManager;

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
    public Invoice createInvoice(PackageInfo packageInfo, Film film) {
        log.info("Create invoice");
        User user =authManager.getUserAuthentication();
        Invoice invoice = new Invoice();
        invoice.setId(new ObjectId().toString());
        invoice.setIssueDate(ZonedDateTime.now());
        invoice.setPaymentStatus(PaymentStatus.PENDING);
        invoice.setUser(user);

        if(film == null && packageInfo == null)
        {
            invoice.setFilms(getListFilm(user));
            invoice.setTotalPrice(getToTalPrice(user));
        }
        else if (film != null && packageInfo == null){
            invoice.getFilms().add(film);
            invoice.setTotalPrice(film.getPrice());
        }
        else if (film == null){
            invoice.setPackageInfo(packageInfo);
            invoice.setTotalPrice(packageInfo.getPrice());
        }

        invoiceRepository.save(invoice);
        log.info("Create invoice successfully");

        return invoice;
    }
    //Hàm lấy danh dách phim (RentalType = RENTAL) từ User
    private List<Film> getListFilm(User user) {
        List<Film> filmList = new ArrayList<>();
        for (Item item : user.getCart()) {
            Film film = item.getFilm();
            if (film.getRentalType() == RentalType.RENTAL)
                filmList.add(film);
        }
        return filmList;
    }
    //Hàm lấy tổng số tiền
    private Double getToTalPrice(User user){
        Double totalPrice = 0.0;
        for (Item item : user.getCart()) {
            totalPrice += item.getFilm().getPrice();
        }
        return totalPrice;
    }
}
