package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.InvoiceResponseDTO;
import com.rental.movie.model.entity.Invoice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FilmMapper filmMapper;

    public InvoiceResponseDTO convertToDTO(Invoice invoice){
        InvoiceResponseDTO invoiceResponse = new InvoiceResponseDTO();
        invoiceResponse.setId(invoice.getId());
        invoiceResponse.setIssueDate(invoice.getIssueDate());
        invoiceResponse.setFilms(invoice.getFilms().stream()
                .map(film -> filmMapper.convertToDTO(film))
                .toList());

        invoiceResponse.setPackageInfo(invoice.getPackageInfo());
        invoiceResponse.setTotalPrice(invoice.getTotalPrice());
        invoiceResponse.setPaymentStatus(invoice.getPaymentStatus());
        invoiceResponse.setUserId(invoice.getUser().getId());

        return invoiceResponse;
    }
}
