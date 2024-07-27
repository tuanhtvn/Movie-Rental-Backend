package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.rental.movie.model.entity.SupportRequest;
import java.util.List;

public interface SupportRequestRepository extends MongoRepository<SupportRequest, String> {
    List<SupportRequest> findByUserId(String userId);
    List<SupportRequest> findByIsResolved(Boolean isResolved);
}