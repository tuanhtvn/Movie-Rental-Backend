package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.TransactionHistoryResponseDTO;
import com.rental.movie.model.entity.TransactionHistory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionHistoryMapper {
    @Autowired
    private ModelMapper modelMapper;
    public TransactionHistoryResponseDTO convertToDTO(TransactionHistory transactionHistory){
        return modelMapper.map(transactionHistory,TransactionHistoryResponseDTO.class);
    }
}
