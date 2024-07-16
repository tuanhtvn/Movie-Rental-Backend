package com.rental.movie.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.model.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ email : ?0, authProvider: ?1 }")
    public Optional<User> findByEmailAndAuthProvider(String email, AuthProvider authProvider);
}
