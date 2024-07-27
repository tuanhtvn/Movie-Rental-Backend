package com.rental.movie.service;

import com.rental.movie.model.entity.RentedFilm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RentalService {
    //Kiểm tra xem có phải là lần click đầu tiên không:
    public boolean checkFirstClickOnRentedFilm(RentedFilm rentedFilm) {
        return rentedFilm.getExpireAt() == null;
    }
}
