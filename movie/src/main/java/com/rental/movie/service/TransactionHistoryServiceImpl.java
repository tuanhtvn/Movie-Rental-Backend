package com.rental.movie.service;

import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.TransactionHistoryResponseDTO;
import com.rental.movie.model.entity.Invoice;
import com.rental.movie.model.entity.TransactionHistory;
import com.rental.movie.repository.TransactionHistoryRepository;
import com.rental.movie.util.mapper.TransactionHistoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TransactionHistoryServiceImpl implements TransactionHistoryService{

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;
    @Override
    public List<TransactionHistoryResponseDTO> getAll() {
        log.info("Get all TransactionHistory ");
        return transactionHistoryRepository.findAll().stream()
                .map(transactionHistory -> transactionHistoryMapper.convertToDTO(transactionHistory))
                .toList();
    }

    @Override
    public TransactionHistoryResponseDTO get(String transactionId) {
        log.info("Get transaction id: {} ",transactionId);
        TransactionHistory transactionHistory = transactionHistoryRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException("Không tìm thấy lịch sử giao dịch", HttpStatus.NOT_FOUND.value()));
        return transactionHistoryMapper.convertToDTO(transactionHistory);
    }
}
