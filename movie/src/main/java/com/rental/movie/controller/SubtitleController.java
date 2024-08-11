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
import com.rental.movie.model.dto.SubtitleRequestDTO;
import com.rental.movie.model.dto.SubtitleResponseDTO;
import com.rental.movie.model.entity.Subtitle;
import com.rental.movie.model.entity.Film;
import com.rental.movie.service.SubtitleService;
import com.rental.movie.exception.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api")
public class SubtitleController {

        @Autowired
        private SubtitleService subtitleService;

        @Operation(summary = "Lấy danh sách phụ đề", description = "Lấy danh sách tất cả phụ đề")
        @ApiResponse(responseCode = "200", description = "Lấy danh sách phụ đề thành công")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @GetMapping("/subtitle/getAll")
        public ResponseEntity<BaseResponse> getAllSubtitles() {
                List<SubtitleResponseDTO> subtitles = subtitleService.getAllSubtitles();
                BaseResponse response = new BaseResponse("Lấy danh sách tất cả phụ đề thành công", HttpStatus.OK.value(), subtitles);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @Operation(summary = "Lấy danh sách phụ đề của phim", description = "Lấy danh sách tất cả phụ đề của 1 bộ phim")
        @ApiResponse(responseCode = "200", description = "Lấy danh sách phụ đề của phim thành công")
        @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @GetMapping("/subtitle/{filmId}")
        public ResponseEntity<BaseResponse> getAllSubtitlesByFilmId(@PathVariable String filmId) {
                List<SubtitleResponseDTO> subtitles = subtitleService.getAllSubtitlesByFilmId(filmId);
                BaseResponse response = new BaseResponse("Lấy danh sách tất cả phụ đề của phim thành công", HttpStatus.OK.value(), subtitles);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @Operation(summary = "Tạo phụ đề", description = "Tạo phụ đề mới")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "404", description = "Tạo thất bại. Có lỗi xảy ra!!!"),
                @ApiResponse(responseCode = "201", description = "Tạo thành công")
        })
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @PostMapping("/subtitle/create")
        public ResponseEntity<BaseResponse> createSubtitle(@Valid @RequestBody SubtitleRequestDTO subtitleDTO) {
                try {
                        SubtitleResponseDTO createdSubtitle = subtitleService.createSubtitle(subtitleDTO);
                        BaseResponse response = new BaseResponse("Tạo thành công", HttpStatus.CREATED.value(), createdSubtitle);
                        return new ResponseEntity<>(response, HttpStatus.CREATED);
                } catch (Exception e) {
                        BaseResponse response = new BaseResponse("Tạo thất bại. Có lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }

        @Operation(summary = "Cập nhật phụ đề", description = "Cập nhật phụ đề theo ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Cập nhật phụ đề thành công"),
                @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
                @ApiResponse(responseCode = "404", description = "Không tìm thấy phụ đề")
        })
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @PutMapping("/subtitle/update/{id}")
        public ResponseEntity<BaseResponse> updateSubtitle(@PathVariable String id, @RequestBody SubtitleRequestDTO subtitleDTO) {
                try {
                        SubtitleResponseDTO updatedSubtitle = subtitleService.updateSubtitle(id, subtitleDTO);
                        BaseResponse response = new BaseResponse("Cập nhật phụ đề thành công", HttpStatus.OK.value(), updatedSubtitle);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (CustomException e) {
                        BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                } catch (Exception e) {
                        BaseResponse response = new BaseResponse("Cập nhật thất bại. Có lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }

        @Operation(summary = "Xóa phụ đề", description = "Xóa phụ đề theo ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Xóa phụ đề thành công"),
                @ApiResponse(responseCode = "404", description = "Không tìm thấy phụ đề")
        })
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @DeleteMapping("/subtitle/delete/{id}")
        public ResponseEntity<BaseResponse> deleteSubtitle(@PathVariable String id) {
                try {
                        subtitleService.deleteSubtitle(id);
                        BaseResponse response = new BaseResponse("Xóa phụ đề thành công", HttpStatus.OK.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (CustomException e) {
                        BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
        }

        @Operation(summary = "Thêm phụ đề", description = "Thêm phụ đề cho phim")
        @ApiResponse(responseCode = "200", description = "Thêm phụ đề thành công")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @PostMapping("/subtitle/{subtitleId}/add/{filmId}")
        public ResponseEntity<BaseResponse> addSubtitle(@PathVariable String subtitleId, @PathVariable String filmId) {
                try {
                        FilmResponseDTO updatedFilm = subtitleService.addSubtitle(subtitleId, filmId);
                        BaseResponse response = new BaseResponse("Thêm phụ đề thành công", HttpStatus.OK.value(), updatedFilm);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (CustomException e) {
                        BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
        }
        @Operation(summary = "Loại bỏ phụ đề", description = "Loại bỏ phụ đề ra khỏi bộ phim")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Loại bỏ phụ đề thành công"),
                @ApiResponse(responseCode = "404", description = "Không tìm thấy phụ đề")
        })
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @DeleteMapping("/subtitle/{subtitleId}/remove/{filmId}")
        public ResponseEntity<BaseResponse> removeSubtitle(@PathVariable String subtitleId, @PathVariable String filmId) {
                try {
                        FilmResponseDTO updatedFilm = subtitleService.removeSubtitle(subtitleId, filmId);
                        BaseResponse response = new BaseResponse("Loại bỏ phụ đề thành công", HttpStatus.OK.value(), updatedFilm);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (CustomException e) {
                        BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
        }

}
