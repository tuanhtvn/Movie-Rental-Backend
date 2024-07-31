package com.rental.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

import com.rental.movie.model.entity.Film;

@Repository
public interface FilmRepository extends MongoRepository<Film, String> {
    public Film findByFilmNameIgnoreCase(String filmName);
    @Query("{ '_id': ?0}")
    public Optional<Film> findByIdDefault(String id);

    @Query("{ '_id': ?0, 'isDeleted': false }")
    public Optional<Film> findById(String id);
    @Query("{ 'fileName': { $regex: ?0, $options: 'i' } }")
    public List<Film> findByKeywords(String keywords);
    @Query("{ 'isActive': true, 'isDeleted': false, 'filmName': { $regex: ?0, $options: 'i' } }")
    public Page<Film> findAllByActived(Pageable pageable, String search);

    @Query("{ 'isDeleted': false, 'filmName': { $regex: ?0, $options: 'i' } }")
    public Page<Film> findAllByNotDeleted(Pageable pageable, String search);
    @Query("{ 'isDeleted': true, 'filmName': { $regex: ?0, $options: 'i' } }")
    public Page<Film> findAllByDeleted(Pageable pageable, String search);
}
