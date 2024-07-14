package com.rental.movie.repository;

import com.rental.movie.model.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AlbumRepository extends MongoRepository<Album, String> {
    public List<Album> findByIsDeletedFalseAndIsActiveTrue();
    public List<Album> findByIsDeletedTrue();
    @Query("{'isActive': true , 'isDeleted': false, $or: [ { '_id': { $regex: ?0, $options: 'i' } }" +
            ", { 'albumName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Album> findContaining(Pageable pageable, String search);
    public List<Album> findByFilm_Id(String filmId);
}