package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.InvoiceResponseDTO;
import com.rental.movie.model.dto.PaymentInfoResponseDTO;
import com.rental.movie.model.entity.Invoice;
import com.rental.movie.model.entity.PaymentInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {
    @Autowired
    private ModelMapper modelMapper;

    public InvoiceResponseDTO convertToDTO(Invoice invoice){
        return modelMapper.map(invoice,InvoiceResponseDTO.class);
    }
}
