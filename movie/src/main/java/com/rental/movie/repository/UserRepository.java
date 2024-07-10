package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rental.movie.models.entity.User;

public interface UserRepository extends MongoRepository<User, String>{
    
}
