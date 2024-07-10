package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rental.movie.models.entity.PackageInfo;

@Repository
public interface PackageInfoRepository extends MongoRepository<PackageInfo, String> {

}
