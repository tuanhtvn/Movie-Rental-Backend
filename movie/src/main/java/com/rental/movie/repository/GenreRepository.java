package com.rental.movie.repository;

import com.rental.movie.model.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends MongoRepository<Genre, String> {
    @Query("{ 'genreName' : { $regex: ?0, $options: 'i' } }")
    public Page<Genre> findAll(Pageable pageable, String search);
    @Query("{'isActive': false , 'isDeleted': false, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'genreName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Genre> findAllInActive(Pageable pageable, String search);
    @Query("{'isActive': true , 'isDeleted': false, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'genreName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Genre> findAllActive(Pageable pageable, String search);
    @Query("{'isDeleted': true, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'genreName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Genre> findAllSoftDelete(Pageable pageable, String search);
    @Override
    @Query("{ '_id': ?0, 'isDeleted': false }")
    public Optional<Genre> findById(String id);
}
