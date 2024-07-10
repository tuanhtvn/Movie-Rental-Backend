package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rental.movie.models.entity.Genre;

public interface GenreRepository extends MongoRepository<Genre, String>{
    
}
