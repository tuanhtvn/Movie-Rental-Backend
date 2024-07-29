package com.rental.movie.service;

import com.rental.movie.common.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface RentalManagementService {
    public ResponseEntity<BaseResponse> getRentalPackageByUserId(String userId);
    public ResponseEntity<BaseResponse> setAutoRenewal(boolean enabled);

    public ResponseEntity<BaseResponse> getRentedFilmsByUserId(String userId);
    public ResponseEntity<BaseResponse> startTimmingRentedFilm(String rentedFilmId);
    public ResponseEntity<BaseResponse> getRentedFilmById(String rentedFilmId);
    public ResponseEntity<BaseResponse> getRentedFilmByFilmName(String filmName);
}
