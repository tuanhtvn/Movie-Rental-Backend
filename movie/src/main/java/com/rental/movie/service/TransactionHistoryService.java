package com.rental.movie.service;

import com.rental.movie.model.dto.InvoiceResponseDTO;
import com.rental.movie.model.dto.TransactionHistoryResponseDTO;

import java.util.List;

public interface TransactionHistoryService {
    public List<TransactionHistoryResponseDTO> getAll();
}
