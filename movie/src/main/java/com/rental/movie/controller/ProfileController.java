package com.rental.movie.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.ProfileRequestDTO;
import com.rental.movie.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProfileController {

        @Autowired
        private ProfileService profileService;

        @Operation(summary = "Tạo hồ sơ", description = "API tạo hồ sơ")
        @ApiResponse(responseCode = "201", description = "Tạo hồ sơ thành công")
        @PostMapping("/profile")
        public ResponseEntity<BaseResponse> createProfile(@RequestBody @Valid ProfileRequestDTO profileRequestDTO) {
                profileService.create(profileRequestDTO);
                BaseResponse response = BaseResponse.builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Tạo hồ sơ thành công")
                                .build();
                return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
        }

        @Operation(summary = "Tải danh sách hồ sơ", description = "API tải danh sách hồ sơ")
        @ApiResponse(responseCode = "200", description = "Tải danh sách hồ sơ thành công")
        @GetMapping("/profiles")
        public ResponseEntity<BaseResponse> getProfiles() {
                BaseResponse response = BaseResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Tải danh sách hồ sơ thành công")
                                .data(profileService.getAll())
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Tải hồ sơ chi tiết", description = "API tải hồ sơ chi tiết")
        @ApiResponse(responseCode = "200", description = "Tải hồ sơ thành công")
        @GetMapping("/profile/{id}")
        public ResponseEntity<BaseResponse> getProfile(@PathVariable("id") String id) {
                BaseResponse response = BaseResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Tải hồ sơ thành công")
                                .data(profileService.get(id))
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Cập nhật hồ sơ", description = "API cập nhật hồ sơ")
        @ApiResponse(responseCode = "200", description = "Cập nhật hồ sơ thành công")
        @PutMapping("/profile/{id}")
        public ResponseEntity<BaseResponse> updateProfile(@PathVariable("id") String id,
                        @RequestBody @Valid ProfileRequestDTO profileRequestDTO) {
                profileService.update(id, profileRequestDTO);
                BaseResponse response = BaseResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Cập nhật hồ sơ thành công")
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Xóa hồ sơ theo ID", description = "API xóa hồ sơ theo ID")
        @ApiResponse(responseCode = "200", description = "Xóa hồ sơ thành công")
        @DeleteMapping("/profile/{id}")
        public ResponseEntity<BaseResponse> deleteProfile(@PathVariable("id") String id) {
                profileService.delete(id);
                BaseResponse response = BaseResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Xóa hồ sơ thành công")
                                .build();
                return ResponseEntity.ok(response);
        }

        // Film
        @Operation(summary = "Tải danh sách phim đã chọn của hồ sơ", description = "API tải danh sách phim đã chọn của hồ sơ")
        @ApiResponse(responseCode = "200", description = "Tải danh sách phim đã chọn thành công")
        @GetMapping("/profile/{profileId}/films")
        public ResponseEntity<BaseResponse> getAllFilm(@PathVariable("profileId") String profileId,
                        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                        @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
                Pageable pageable = PageRequest.of(page, size);
                Page<FilmResponseDTO> data = profileService.getAllFilm(profileId, pageable);
                BaseResponse response = BaseResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Tải danh sách phim đã chọn thành công")
                                .data(new HashMap<String, Object>() {
                                        {
                                                put("Films", data.getContent());
                                                put("CurrentPage", data.getNumber());
                                                put("NumberOfItems", data.getNumberOfElements());
                                                put("TotalItems", data.getTotalElements());
                                                put("TotalPages", data.getTotalPages());
                                        }
                                })
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Thêm phim vào hồ sơ", description = "API thêm phim vào hồ sơ")
        @ApiResponse(responseCode = "200", description = "Thêm phim vào hồ sơ thành công")
        @PostMapping("/profile/{profileId}/film/{filmId}")
        public ResponseEntity<BaseResponse> pushFilm(@PathVariable("profileId") String profileId,
                        @PathVariable("filmId") String filmId) {
                profileService.pushFilm(profileId, filmId);
                BaseResponse response = BaseResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Thêm phim vào hồ sơ thành công")
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Xóa phim khỏi hồ sơ", description = "API xóa phim khỏi hồ sơ")
        @ApiResponse(responseCode = "200", description = "Xóa phim khỏi hồ sơ thành công")
        @DeleteMapping("/profile/{profileId}/film/{filmId}")
        public ResponseEntity<BaseResponse> popFilm(@PathVariable("profileId") String profileId,
                        @PathVariable("filmId") String filmId) {
                profileService.popFilm(profileId, filmId);
                BaseResponse response = BaseResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Xóa phim khỏi hồ sơ thành công")
                                .build();
                return ResponseEntity.ok(response);
        }
}
