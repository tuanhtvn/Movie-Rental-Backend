package com.rental.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.rental.movie.model.entity.PackageInfo;

@Repository
public interface PackageInfoRepository extends MongoRepository<PackageInfo, String> {
    public Page<PackageInfo> findByIsDeletedFalse(Pageable pageable);
    public Page<PackageInfo> findByIsDeletedTrue(Pageable pageable);
    public Page<PackageInfo> findByIsActiveAndIsDeletedFalse(Pageable pageable, Boolean flag);
    @Query("{'isActive':  true, 'isDeleted':  false, 'packageName': {$regex: ?0, $options: 'i'}}")
    public Page<PackageInfo> findByPackageNameContainingIgnoreCase(Pageable pageable, String input);
    public PackageInfo findByIdAndIsActiveTrueAndIsDeletedFalse(String id);
}
