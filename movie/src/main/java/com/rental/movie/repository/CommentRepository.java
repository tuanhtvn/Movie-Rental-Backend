package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import com.rental.movie.model.entity.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    Page<Comment> findByFilmId(String filmId, Pageable pageable);
}
