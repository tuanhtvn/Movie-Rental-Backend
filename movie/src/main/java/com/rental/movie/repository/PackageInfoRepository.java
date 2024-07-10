package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rental.movie.models.entity.PackageInfo;

public interface PackageInfoRepository extends MongoRepository<PackageInfo, String>{
    
}
