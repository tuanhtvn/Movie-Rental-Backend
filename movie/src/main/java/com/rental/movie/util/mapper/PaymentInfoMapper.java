package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.PaymentInfoRepuestDTO;
import com.rental.movie.model.dto.PaymentInfoResponseDTO;
import com.rental.movie.model.entity.PaymentInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentInfoMapper {
    @Autowired
    private ModelMapper modelMapper;

    public PaymentInfoResponseDTO convertToDTO(PaymentInfo paymentInfo){
        return modelMapper.map(paymentInfo,PaymentInfoResponseDTO.class);
    }
    public PaymentInfo convertToEntity(PaymentInfoRepuestDTO paymentInfoRepuestDTO){
        return modelMapper.map(paymentInfoRepuestDTO,PaymentInfo.class);
    }
    public PaymentInfo convertToEntity(PaymentInfoRepuestDTO paymentInfoRepuestDTO,PaymentInfo paymentInfo){
        modelMapper.map(paymentInfoRepuestDTO,paymentInfo);
        return paymentInfo;
    }
}
