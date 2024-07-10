package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rental.movie.models.entity.SupportRequest;

public interface SupportRequestRepository extends MongoRepository<SupportRequest, String>{
    
}
