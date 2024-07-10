package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rental.movie.models.entity.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
    
}
