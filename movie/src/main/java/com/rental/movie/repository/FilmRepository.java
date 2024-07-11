package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rental.movie.model.entity.Film;

@Repository
public interface FilmRepository extends MongoRepository<Film, String> {

}
