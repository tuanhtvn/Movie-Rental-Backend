package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rental.movie.model.entity.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

}
