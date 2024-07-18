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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<BaseResponse> getAllBanners(Pageable pageable) {
        Page<BannerDTO> bannerDTOs = bannerRepository.findByIsDeletedFalse(pageable)
                .map(bannerMapper::convertToDTO);
        BaseResponse baseResponse;
        if (bannerDTOs.isEmpty()) {
            baseResponse = BaseResponse.builder()
                    .message("Không tìm thấy banner nào.")
                    .status(HttpStatus.OK.value())
                    .data(null)
                    .build();
        } else {
            baseResponse = BaseResponse.builder()
                    .message("Tìm thấy danh sách banner.")
                    .status(HttpStatus.OK.value())
                    .data(bannerDTOs.getContent())
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @Override
    public ResponseEntity<BaseResponse> createBanner(BannerCreationDTO bannerCreationDTO) {
        Banner banner = bannerMapper.convertToEntity(bannerCreationDTO);
        BannerDTO responseBanner = bannerMapper.convertToDTO(
                bannerRepository.save(banner));

        BaseResponse baseResponse = BaseResponse.builder()
                .message("Tạo banner thành công.")
                .status(HttpStatus.CREATED.value())
                .data(responseBanner)
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
                        new BaseResponse("Không tìm thấy phim có id này!",
                                HttpStatus.NOT_FOUND.value(),
                                null));
            }

            Banner newBanner = bannerFound.get();
            newBanner.setImageUrl(bannerCreationDTO.getImageUrl());
            newBanner.setFilm(filmFound);
            newBanner.setIsActive(bannerCreationDTO.getIsActive());

            BannerDTO responseBanner = bannerMapper.convertToDTO(
                    bannerRepository.save(newBanner));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse("Cập nhật banner thành công.", HttpStatus.OK.value(), responseBanner));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy banner!", HttpStatus.NOT_FOUND.value(), null));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> softDeleteById(String id) {
        Optional<Banner> bannerFound = bannerRepository.findById(id);
        if (bannerFound.isPresent()) {
            Banner bannerToDelete = bannerFound.get();
            bannerToDelete.setIsDeleted(true);

            BannerDTO responseBanner = bannerMapper.convertToDTO(
                    bannerRepository.save(bannerToDelete));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse("Xoá mềm banner thành công.", HttpStatus.OK.value(), responseBanner));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy banner!", HttpStatus.NOT_FOUND.value(), null));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> findByFilmNameOrFilmId(Pageable pageable, String input) {
        String trimmedInput = input.trim();

        Page<BannerDTO> bannerDTOs;
        Film filmFound = filmRepository.findByFilmNameIgnoreCase(trimmedInput);
        if (filmFound != null) {
            bannerDTOs = bannerRepository.findByFilmId_AndIsDeletedFalse(pageable, filmFound.getId())
                    .map(bannerMapper::convertToDTO);
        } else {
            //nếu tìm bằng tên phim không thấy, thử tìm bằng id phim
            bannerDTOs = bannerRepository.findByFilmId_AndIsDeletedFalse(pageable, trimmedInput)
                    .map(bannerMapper::convertToDTO);
        }

        if (bannerDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy banner nào!",
                            HttpStatus.NOT_FOUND.value(),
                            null));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse("Tìm thấy banner.",
                            HttpStatus.OK.value(),
                            bannerDTOs.getContent()));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> getSoftDeletedBanners(Pageable pageable) {
        Page<BannerDTO> bannerDTOs = bannerRepository.findByIsDeletedTrue(pageable)
                .map(bannerMapper::convertToDTO);

        BaseResponse baseResponse;
        if (bannerDTOs.isEmpty()) {
            baseResponse = BaseResponse.builder()
                    .message("Không tìm thấy banner nào.")
                    .status(HttpStatus.OK.value())
                    .data(null)
                    .build();
        } else {
            baseResponse = BaseResponse.builder()
                    .message("Tìm thấy danh sách banner.")
                    .status(HttpStatus.OK.value())
                    .data(bannerDTOs.getContent())
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

            BannerDTO responseBanner = bannerMapper.convertToDTO(
                    bannerRepository.save(banner));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse("Khôi phục banner thành công.", HttpStatus.OK.value(), responseBanner));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy banner.", HttpStatus.NOT_FOUND.value(), null));
        }
    }

    @Override
    public ResponseEntity<BaseResponse> getAllActiveOrInactive(Pageable pageable, Boolean flag) {
        Page<BannerDTO> bannerDTOs = bannerRepository.findByIsActiveAndIsDeletedFalse(pageable, flag)
                .map(bannerMapper::convertToDTO);
        if (bannerDTOs.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy banner nào.", HttpStatus.NOT_FOUND.value(), null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse("OK.", HttpStatus.OK.value(), bannerDTOs.getContent())
        );
    }

    @Override
    public ResponseEntity<BaseResponse> toggleActiveStatus(String id) {
        Optional<Banner> banner = bannerRepository.findById(id);
        if (banner.isPresent()){
            Banner newBanner = banner.get();
            newBanner.setIsActive(!newBanner.getIsActive());

            String message = "Cập nhật thành công. Trạng thái hiện tại: "
                    + (newBanner.getIsActive() ? "Active" : "Inactive");
            BannerDTO responseBanner = bannerMapper.convertToDTO(
                    bannerRepository.save(newBanner));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(message, HttpStatus.OK.value(), responseBanner)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy banner.", HttpStatus.NOT_FOUND.value(), null)
            );
        }
    }


}
