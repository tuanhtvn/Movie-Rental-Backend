package com.rental.movie.repository;

import com.rental.movie.model.entity.Banner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends MongoRepository<Banner, String> {
    public List<Banner> findByIsDeletedFalse();
    public List<Banner> findByFilm_Id(String idFilm);
    public List<Banner> findByIsDeletedTrue();
}
