package com.rental.movie.service;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.BannerCreationDTO;
import com.rental.movie.model.entity.Banner;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BannerService {
    public ResponseEntity<BaseResponse> getAllBanners();
    public ResponseEntity<BaseResponse> createBanner(BannerCreationDTO bannerCreationDTO);
    public ResponseEntity<BaseResponse> updateBanner(String id, BannerCreationDTO bannerCreationDTO);
    public ResponseEntity<BaseResponse> softDeleteById(String id);
    public ResponseEntity<BaseResponse> findByFilmNameOrFilmId(String input);
    public ResponseEntity<BaseResponse> getSoftDeletedBanners();
    public ResponseEntity<BaseResponse> restoreBannerById(String id);
}
