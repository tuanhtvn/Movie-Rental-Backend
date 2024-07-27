package com.rental.movie.service;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.entity.RentalPackage;
import com.rental.movie.model.entity.RentedFilm;
import com.rental.movie.model.entity.User;
import org.springframework.http.ResponseEntity;

import java.awt.print.Pageable;
import java.time.ZonedDateTime;

public interface RentalManagementService {
    public ResponseEntity<BaseResponse> getRentalPackageByUserId(String userId);
    public ResponseEntity<BaseResponse> disableAutoRenewal();
    public ResponseEntity<BaseResponse> enableAutoRenewal();
    public ResponseEntity<BaseResponse> getRentedFilmsByUserId(String userId);
    public ResponseEntity<BaseResponse> startTimmingRentedFilm(String rentedFilmId);
    public ResponseEntity<BaseResponse> getRentedFilmById(String rentedFilmId);
    public ResponseEntity<BaseResponse> getRentedFilmByFilmName(String filmName);
}
