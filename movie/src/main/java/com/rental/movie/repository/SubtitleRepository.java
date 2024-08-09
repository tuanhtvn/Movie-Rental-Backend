package com.rental.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

import com.rental.movie.model.entity.Subtitle;

@Repository
public interface SubtitleRepository extends MongoRepository<Subtitle, String> {

    //List<Subtitle> findByFilmId(String filmId);
}
