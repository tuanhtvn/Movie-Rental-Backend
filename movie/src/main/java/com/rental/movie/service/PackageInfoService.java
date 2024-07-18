package com.rental.movie.service;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.PackageInfoRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PackageInfoService {
    public ResponseEntity<BaseResponse> createPackage(PackageInfoRequestDTO packageInfoRequestDTO);
    public ResponseEntity<BaseResponse> getAllPackage(Pageable pageable);
    public ResponseEntity<BaseResponse> updatePackageById(String id, PackageInfoRequestDTO packageInfoRequestDTO);
    public ResponseEntity<BaseResponse> softDeletePackageById(String id);
    public ResponseEntity<BaseResponse> toggleActiveStatus(String id);
    public ResponseEntity<BaseResponse> getSoftDeletedPackages(Pageable pageable);
    public ResponseEntity<BaseResponse> restoreById(String id);
    public ResponseEntity<BaseResponse> getAllActiveOrInactive(Pageable pageable, Boolean flag);
    public ResponseEntity<BaseResponse> findByNameContaining(Pageable pageable, String pktName);
}
