package com.rental.movie.repository;

import com.rental.movie.model.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends MongoRepository<Banner, String> {
    public Page<Banner> findByIsDeletedFalse(Pageable pageable);
    public Page<Banner> findByFilmId_AndIsDeletedFalse(Pageable pageable, String idFilm);
    public Page<Banner> findByIsDeletedTrue(Pageable pageable);
    public Page<Banner> findByIsActiveAndIsDeletedFalse(Pageable pageable, Boolean flag);
}
