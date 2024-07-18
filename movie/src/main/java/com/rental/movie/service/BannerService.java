package com.rental.movie.service;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.BannerCreationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BannerService {
    public ResponseEntity<BaseResponse> getAllBanners(Pageable pageable);

    public ResponseEntity<BaseResponse> createBanner(BannerCreationDTO bannerCreationDTO);

    public ResponseEntity<BaseResponse> updateBanner(String id, BannerCreationDTO bannerCreationDTO);

    public ResponseEntity<BaseResponse> softDeleteById(String id);

    public ResponseEntity<BaseResponse> findByFilmNameOrFilmId(Pageable pageable, String input);

    public ResponseEntity<BaseResponse> getSoftDeletedBanners(Pageable pageable);

    public ResponseEntity<BaseResponse> restoreBannerById(String id);
    public ResponseEntity<BaseResponse> getAllActiveOrInactive(Pageable pageable, Boolean flag);
    public ResponseEntity<BaseResponse> toggleActiveStatus(String id);
}
