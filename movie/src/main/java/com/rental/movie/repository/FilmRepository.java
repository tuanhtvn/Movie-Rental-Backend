package com.rental.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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

    @Query("{ '_id': ?0, 'isDeleted': false, 'isActive': true }")
    public Optional<Film> findById(String id);

    @Query("{ 'filmName': { $regex: ?0, $options: 'i' }, 'isDeleted': false, 'isActive': true }")
    public List<Film> findByKeywords(String keywords);
    @Query("{ 'isActive': true, 'isDeleted': false, $or: [ { '_id': { $regex: ?0, $options: 'i' } }," +
            " { 'filmName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Film> findAllByActived(Pageable pageable, String search);

    @Query("{ 'isActive': true, 'isDeleted': false }")
    public List<Film> findByActived();

    @Query("{ 'isDeleted': false, $or: [ { '_id': { $regex: ?0, $options: 'i' } }, { 'filmName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Film> findAllByNotDeleted(Pageable pageable, String search);
    @Query("{ 'isDeleted': true, $or: [ { '_id': { $regex: ?0, $options: 'i' } }, { 'filmName': { $regex: ?0, $options: 'i' } } ] }")
    public Page<Film> findAllByDeleted(Pageable pageable, String search);

    @Query("{ 'subtitles.id': ?0 }")
    List<Film> findBySubtitlesId(String subtitleId);

    @Query("{ 'narrations.id': ?0 }")
    List<Film> findByNarrationsId(String narrationId);

    @Query("{ 'comments.id': ?0 }")
    List<Film> findByCommentsId(String commentId);
    @Query(value = "{}", sort = "{ 'numberOfViews': -1 }")
    List<Film> findTop5Film(PageRequest pageRequest);
}
