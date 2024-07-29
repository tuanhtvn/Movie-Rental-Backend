package com.rental.movie.service;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.common.IAuthentication;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.RentalPackageResponseDTO;
import com.rental.movie.model.dto.RentedFilmResponseDTO;
import com.rental.movie.model.entity.Film;
import com.rental.movie.model.entity.RentalPackage;
import com.rental.movie.model.entity.RentedFilm;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.repository.UserRepository;
import com.rental.movie.util.mapper.RentalPackageMapper;
import com.rental.movie.util.mapper.RentedFilmMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RentalManagementServiceImpl implements RentalManagementService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private RentalPackageMapper rentalPackageMapper;
    @Autowired
    private RentedFilmMapper rentedFilmMapper;
    @Autowired
    private RentalService rentalService;
    @Autowired
    private IAuthentication authManager;

    //Gói Thuê
    @Override
    public ResponseEntity<BaseResponse> getRentalPackageByUserId(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new CustomException("Không tìm thấy người dùng!", HttpStatus.NOT_FOUND.value());
        }
        RentalPackage rentalPackage = user.getRentalPackage();
        if (rentalPackage == null) {
            throw new CustomException("Không tìm thấy gói thuê!", HttpStatus.NOT_FOUND.value());
        }

        RentalPackageResponseDTO rentalPackageResponse = rentalPackageMapper.convertToDTO(rentalPackage);
        return ResponseEntity.ok(
                new BaseResponse("Tìm thấy gói thuê.", HttpStatus.OK.value(), rentalPackageResponse)
        );
    }

    @Override
    public ResponseEntity<BaseResponse> setAutoRenewal(boolean enabled) {
        User user = authManager.getUserAuthentication();
        if (user == null) {
            throw new CustomException("Không tìm thấy người dùng!", HttpStatus.NOT_FOUND.value());
        }
        RentalPackage rentalPackage = user.getRentalPackage();
        if (rentalPackage == null) {
            throw new CustomException("Không tìm thấy gói thuê!", HttpStatus.NOT_FOUND.value());
        }

        rentalPackage.setIsRenewal(enabled);
        user.setRentalPackage(rentalPackage);
        userRepository.save(user);

        String message = "Đã " + (rentalPackage.getIsRenewal() ? "bật" : "huỷ") +
                " gia hạn gói thuê tự động.";

        return ResponseEntity.ok(
                new BaseResponse(message,
                        HttpStatus.OK.value(),
                        rentalPackageMapper.convertToDTO(rentalPackage))
        );
    }

    //Phim Thuê
    @Override
    public ResponseEntity<BaseResponse> getRentedFilmsByUserId(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new CustomException("Không tìm thấy người dùng!", HttpStatus.NOT_FOUND.value());
        }

        List<RentedFilm> rentedFilms = user.getRentedFilms();
        if (rentedFilms.isEmpty()) {
            throw new CustomException("Không tìm thấy phim thuê!", HttpStatus.NOT_FOUND.value());
        }

        List<RentedFilmResponseDTO> rentedFilmsDTO = rentedFilms
                .stream().map(rentedFilmMapper::convertToDTO).collect(Collectors.toList());

        // sắp xếp theo thời gian sử dụng còn lại tăng dần
        Collections.sort(rentedFilmsDTO, Comparator.comparingLong(RentedFilmResponseDTO::getMinutesLeft));

        return ResponseEntity.ok(
                new BaseResponse("Tìm thấy danh sách phim thuê.", HttpStatus.OK.value(), rentedFilmsDTO)
        );
    }

    @Override
    public ResponseEntity<BaseResponse> startTimmingRentedFilm(String rentedFilmId) {
        User user = authManager.getUserAuthentication();
        if (user == null) {
            throw new CustomException("Không tìm thấy người dùng!", HttpStatus.NOT_FOUND.value());
        }

        List<RentedFilm> rentedFilms = user.getRentedFilms();
        if (rentedFilms.isEmpty()) {
            throw new CustomException("Không tìm thấy phim thuê!", HttpStatus.NOT_FOUND.value());
        }

        for (RentedFilm rentedFilm : rentedFilms) {
            if (rentedFilm.getId().equals(rentedFilmId)) {
                //kiểm tra xem phim thuê này đã được kích hoạt trước đó chưa
                if (!rentalService.checkFirstClickOnRentedFilm(rentedFilm)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new BaseResponse("Phim thuê này đã được kích hoạt tính giờ trước đó rồi!",
                                    HttpStatus.BAD_REQUEST.value(), null)
                    );
                }
                rentedFilm.setExpireAt(ZonedDateTime.now().plusHours(48)); //set giờ hết hạn là 48 giờ sau đó

                user.setRentedFilms(rentedFilms);
                userRepository.save(user);

                RentedFilmResponseDTO responseDTO = rentedFilmMapper.convertToDTO(rentedFilm);
                return ResponseEntity.ok(
                        new BaseResponse("Đã kích hoạt tính giờ phim thuê.", HttpStatus.OK.value(), responseDTO)
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new BaseResponse("Không tìm thấy phim thuê.", HttpStatus.NOT_FOUND.value(), null)
        );
    }

    @Override
    public ResponseEntity<BaseResponse> getRentedFilmById(String rentedFilmId) {
        List<RentedFilm> rentedFilms = rentalService.getRentedFilmsByCurrentUser();

        for (RentedFilm rentedFilm : rentedFilms) {
            if (rentedFilm.getId().equals(rentedFilmId))

                return ResponseEntity.ok(
                        new BaseResponse("Tìm thấy phim thuê.",
                                HttpStatus.OK.value(),
                                rentedFilmMapper.convertToDTO(rentedFilm))
                );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new BaseResponse("Không tìm thấy phim thuê.", HttpStatus.NOT_FOUND.value(), null)
        );
    }

    @Override
    public ResponseEntity<BaseResponse> getRentedFilmByFilmName(String filmName) {
        List<RentedFilm> rentedFilms = rentalService.getRentedFilmsByCurrentUser();
        Film film = filmRepository.findByFilmNameIgnoreCase(filmName);

        if (film != null) {
            for (RentedFilm rentedFilm : rentedFilms) {
                if (rentedFilm.getFilm().equals(film))
                    return ResponseEntity.ok(
                            new BaseResponse("Tìm thấy phim thuê.",
                                    HttpStatus.OK.value(),
                                    rentedFilmMapper.convertToDTO(rentedFilm))
                    );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new BaseResponse("Không tìm thấy phim thuê.", HttpStatus.NOT_FOUND.value(), null)
        );
    }

}
