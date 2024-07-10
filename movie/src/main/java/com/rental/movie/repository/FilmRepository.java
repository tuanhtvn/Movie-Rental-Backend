package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rental.movie.models.entity.Film;

public interface FilmRepository extends MongoRepository<Film, String>{
    
}
