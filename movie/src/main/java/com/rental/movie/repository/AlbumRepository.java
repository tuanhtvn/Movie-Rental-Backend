package com.rental.movie.repository;

import com.rental.movie.model.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AlbumRepository extends MongoRepository<Album, String> {
    @Query("{ 'albumName' : { $regex: ?0, $options: 'i' } }")
    public Page<Album> findAll(Pageable pageable, String search);
    @Query("{'isActive': false , 'isDeleted': false, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'albumName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Album> findAllInActive(Pageable pageable, String search);
    @Query("{'isActive': true , 'isDeleted': false, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'albumName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Album> findAllActive(Pageable pageable, String search);
    @Query("{'isDeleted': true, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'albumName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Album> findAllSoftDelete(Pageable pageable, String search);
    @Override
    @Query("{ '_id': ?0, 'isDeleted': false }")
    public Optional<Album> findById(String id);
}