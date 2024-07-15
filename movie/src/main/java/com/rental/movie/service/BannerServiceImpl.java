package com.rental.movie.service;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.BannerCreationDTO;
import com.rental.movie.model.dto.BannerDTO;
import com.rental.movie.model.entity.Banner;
import com.rental.movie.model.entity.Film;
import com.rental.movie.repository.BannerRepository;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.util.mapper.BannerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public ResponseEntity<BaseResponse> getAllBanners() {
        List<BannerDTO> listFound = bannerRepository.findByIsDeletedFalse()
                .stream()
                .map(bannerMapper::convertToDTO)
                .collect(Collectors.toList());

        BaseResponse baseResponse;
        if (listFound.isEmpty()) {
            baseResponse = BaseResponse.builder()
                    .message("No banners found.")
                    .status(HttpStatus.OK.value())
                    .data(null)
                    .build();
        } else {
            baseResponse = BaseResponse.builder()
                    .message("OK! List banners found.")
                    .status(HttpStatus.OK.value())
                    .data(listFound)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @Override
    public ResponseEntity<BaseResponse> createBanner(BannerCreationDTO bannerCreationDTO) {
        Banner banner = bannerMapper.convertToEntity(bannerCreationDTO);
        bannerRepository.save(banner);

        BaseResponse baseResponse = BaseResponse.builder()
                .message("Banner created successfully.")
                .status(HttpStatus.CREATED.value())
                .data(bannerCreationDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @Override
    public ResponseEntity<BaseResponse> updateBanner(String id, BannerCreationDTO bannerCreationDTO) {
        Optional<Banner> bannerFound = bannerRepository.findById(id);
        if (bannerFound.isPresent()) {
            Film filmFound = filmRepository.findById(bannerCreationDTO.getIdFilm()).orElse(null);
            if (filmFound == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse("Film not found!", HttpStatus.NOT_FOUND.value(), null));
            }

            Banner newBanner = bannerFound.get();
            newBanner.setImageUrl(bannerCreationDTO.getImageUrl());
            newBanner.setFilm(filmFound);
            newBanner.setIsActive(bannerCreationDTO.getIsActive());

            bannerRepository.save(newBanner);

            BannerDTO responseBanner = bannerMapper.convertToDTO(newBanner);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse("Updated banner successfully.", HttpStatus.OK.value(), responseBanner));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Banner not found!", HttpStatus.NOT_FOUND.value(), null));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> softDeleteById(String id) {
        Optional<Banner> bannerFound = bannerRepository.findById(id);
        if (bannerFound.isPresent()) {
            Banner bannerToDelete = bannerFound.get();
            bannerToDelete.setIsDeleted(true);

            bannerRepository.save(bannerToDelete);

            BannerDTO responseBanner = bannerMapper.convertToDTO(bannerToDelete);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse("Soft deleted banner succesfully.", HttpStatus.OK.value(), responseBanner));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Failed! Banner not found!", HttpStatus.NOT_FOUND.value(), null));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> findByFilmNameOrFilmId(String input) {
        String trimmedInput = input.trim();
        List<Banner> bannersFound = new ArrayList<>();

        Film filmFound = filmRepository.findByFilmNameIgnoreCase(trimmedInput);
        if (filmFound != null) {
            bannersFound = (List<Banner>) bannerRepository.findByFilm_Id(filmFound.getId());
        } else {
            // nếu tìm bằng tên phim không thấy, thử tìm bằng id phim
            bannersFound = bannerRepository.findByFilm_Id(trimmedInput);
        }

        if (bannersFound.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Failed! Banners not found!", HttpStatus.NOT_FOUND.value(), null));
        } else {
            List<BannerDTO> responseBanners = bannersFound.stream()
                    .map(bannerMapper::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse("Banners found.", HttpStatus.OK.value(), responseBanners));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> getSoftDeletedBanners() {
        List<BannerDTO> listFound = bannerRepository.findByIsDeletedTrue()
                .stream()
                .map(bannerMapper::convertToDTO)
                .collect(Collectors.toList());

        BaseResponse baseResponse;
        if (listFound.isEmpty()) {
            baseResponse = BaseResponse.builder()
                    .message("No banners found.")
                    .status(HttpStatus.OK.value())
                    .data(null)
                    .build();
        } else {
            baseResponse = BaseResponse.builder()
                    .message("OK! List banners found.")
                    .status(HttpStatus.OK.value())
                    .data(listFound)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @Override
    public ResponseEntity<BaseResponse> restoreBannerById(String id) {
        Optional<Banner> bannerFound = bannerRepository.findById(id);
        if (bannerFound.isPresent()) {
            Banner banner = bannerFound.get();
            banner.setIsDeleted(false);

            bannerRepository.save(banner);

            BannerDTO responseBanner = bannerMapper.convertToDTO(banner);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse("OK! Restore banner successfully", HttpStatus.OK.value(), responseBanner));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Failed! Banner not found.", HttpStatus.NOT_FOUND.value(), null));
        }
    }

}
