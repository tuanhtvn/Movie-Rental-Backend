package com.rental.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.rental.movie.model.entity.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CategoryRepository extends MongoRepository<Category, String> {
    @Query("{ 'categoryName' : { $regex: ?0, $options: 'i' } }")
    public Page<Category> findAll(Pageable pageable, String search);
    @Query("{'isActive': false , 'isDeleted': false, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'categoryName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Category> findAllInActive(Pageable pageable, String search);
    @Query("{'isActive': true , 'isDeleted': false, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'categoryName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Category> findAllActive(Pageable pageable, String search);
    @Query("{'isDeleted': true, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'categoryName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Category> findAllSoftDelete(Pageable pageable, String search);
    @Override
    @Query("{ '_id': ?0, 'isDeleted': false }")
    public Optional<Category> findById(String id);
}
