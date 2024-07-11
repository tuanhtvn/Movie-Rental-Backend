package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rental.movie.model.entity.PackageInfo;

@Repository
public interface PackageInfoRepository extends MongoRepository<PackageInfo, String> {

}
