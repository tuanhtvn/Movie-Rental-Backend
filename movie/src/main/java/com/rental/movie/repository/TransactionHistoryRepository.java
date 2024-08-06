package com.rental.movie.repository;

import com.rental.movie.model.entity.TransactionHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends MongoRepository<TransactionHistory, String> {
}
