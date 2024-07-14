package com.rental.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.rental.movie.model.entity.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends MongoRepository<Category, String> {
    public List<Category> findByIsDeletedFalseAndIsActiveTrue();
    public List<Category> findByIsDeletedTrue();
    @Query("{'isActive': true , 'isDeleted': false, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'categoryName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Category> findContaining(Pageable pageable, String search);
}
