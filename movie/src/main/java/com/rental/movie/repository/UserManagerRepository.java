package com.rental.movie.repository;

import com.rental.movie.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserManagerRepository extends MongoRepository<User, String> {
}
