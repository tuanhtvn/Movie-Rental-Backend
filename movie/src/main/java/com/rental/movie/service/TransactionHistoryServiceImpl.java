package com.rental.movie.service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.model.dto.TransactionHistoryResponseDTO;
import com.rental.movie.model.entity.User;
import com.rental.movie.util.mapper.TransactionHistoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TransactionHistoryServiceImpl implements TransactionHistoryService{

    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;
    @Autowired
    private IAuthentication authentication;
    @Override
    public List<TransactionHistoryResponseDTO> getAll() {
        log.info("Get all TransactionHistory ");
        User user = authentication.getUserAuthentication();
        return user.getTransactionHistories().stream()
                .map(transactionHistory -> transactionHistoryMapper.convertToDTO(transactionHistory))
                .toList();
    }

}
