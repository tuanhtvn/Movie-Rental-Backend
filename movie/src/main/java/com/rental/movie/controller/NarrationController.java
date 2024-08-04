package com.rental.movie.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.dto.NarrationRequestDTO;
import com.rental.movie.model.dto.NarrationResponseDTO;
import com.rental.movie.model.entity.Narration;
import com.rental.movie.model.entity.Film;
import com.rental.movie.service.NarrationService;
import com.rental.movie.exception.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api")
public class NarrationController {

        @Autowired
        private NarrationService narrationService;

        @Operation(summary = "Lấy danh sách thuyết minh", description = "Lấy danh sách tất cả thuyết minh")
        @ApiResponse(responseCode = "200", description = "Lấy danh sách thuyết minh thành công")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @GetMapping("/narration/getAll")
        public ResponseEntity<BaseResponse> getAllNarrations() {
                List<NarrationResponseDTO> narrations = narrationService.getAllNarrations();
                BaseResponse response = new BaseResponse("Lấy danh sách tất cả thuyết minh thành công", HttpStatus.OK.value(), narrations);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @Operation(summary = "Lấy danh sách thuyết minh của phim", description = "Lấy danh sách tất cả thuyết minh của 1 bộ phim")
        @ApiResponse(responseCode = "200", description = "Lấy danh sách thuyết minh của phim thành công")
        @GetMapping("/auth/narration/{filmId}")
        public ResponseEntity<BaseResponse> getAllNarrationsByFilmId(@PathVariable String filmId) {
                List<NarrationResponseDTO> narrations = narrationService.getAllNarrationsByFilmId(filmId);
                BaseResponse response = new BaseResponse("Lấy danh sách tất cả thuyết minh của phim thành công", HttpStatus.OK.value(), narrations);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @Operation(summary = "Tạo thuyết minh", description = "Tạo thuyết minh mới")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "404", description = "Tạo thất bại. Có lỗi xảy ra!!!"),
                @ApiResponse(responseCode = "201", description = "Tạo thành công")
        })
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @PostMapping("/narration/create")
        public ResponseEntity<BaseResponse> createNarration(@Valid @RequestBody NarrationRequestDTO narrationDTO) {
                try {
                        NarrationResponseDTO createdNarration = narrationService.createNarration(narrationDTO);
                        BaseResponse response = new BaseResponse("Tạo thành công", HttpStatus.CREATED.value(), createdNarration);
                        return new ResponseEntity<>(response, HttpStatus.CREATED);
                } catch (Exception e) {
                        BaseResponse response = new BaseResponse("Tạo thất bại. Có lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }

        @Operation(summary = "Cập nhật thuyết minh", description = "Cập nhật thuyết minh theo ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Cập nhật thuyết minh thành công"),
                @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
                @ApiResponse(responseCode = "404", description = "Không tìm thấy thuyết minh")
        })
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @PutMapping("/narration/update/{id}")
        public ResponseEntity<BaseResponse> updateNarration(@PathVariable String id, @RequestBody NarrationRequestDTO narrationDTO) {
                try {
                        NarrationResponseDTO updatedNarration = narrationService.updateNarration(id, narrationDTO);
                        BaseResponse response = new BaseResponse("Cập nhật thuyết minh thành công", HttpStatus.OK.value(), updatedNarration);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (CustomException e) {
                        BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                } catch (Exception e) {
                        BaseResponse response = new BaseResponse("Cập nhật thất bại. Có lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }

        @Operation(summary = "Xóa thuyết minh", description = "Xóa thuyết minh theo ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Xóa thuyết minh thành công"),
                @ApiResponse(responseCode = "404", description = "Không tìm thấy thuyết minh")
        })
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @DeleteMapping("/narration/delete/{id}")
        public ResponseEntity<BaseResponse> deleteNarration(@PathVariable String id) {
                try {
                        narrationService.deleteNarration(id);
                        BaseResponse response = new BaseResponse("Xóa thuyết minh thành công", HttpStatus.OK.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (CustomException e) {
                        BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
        }

        @Operation(summary = "Thêm thuyết minh", description = "Thêm thuyết minh cho phim")
        @ApiResponse(responseCode = "200", description = "Thêm thuyết minh thành công")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @PostMapping("/narration/{filmId}/add")
        public ResponseEntity<BaseResponse> addNarration(@RequestBody NarrationRequestDTO narrationDTO, @PathVariable String filmId) {
                try {
                        FilmResponseDTO updatedFilm = narrationService.addNarration(narrationDTO, filmId);
                        BaseResponse response = new BaseResponse("Thêm thuyết minh thành công", HttpStatus.OK.value(), updatedFilm);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (CustomException e) {
                        BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
        }
        @Operation(summary = "Loại bỏ thuyết minh", description = "Loại bỏ thuyết minh ra khỏi bộ phim")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Loại bỏ thuyết minh thành công"),
                @ApiResponse(responseCode = "404", description = "Không tìm thấy thuyết minh")
        })
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @DeleteMapping("/narration/{filmId}/remove/{narrationId}")
        public ResponseEntity<BaseResponse> removeNarration(@PathVariable String narrationId, @PathVariable String filmId) {
                try {
                        FilmResponseDTO updatedFilm = narrationService.removeNarration(filmId, narrationId);
                        BaseResponse response = new BaseResponse("Loại bỏ thuyết minh thành công", HttpStatus.OK.value(), updatedFilm);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (CustomException e) {
                        BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
        }

}
