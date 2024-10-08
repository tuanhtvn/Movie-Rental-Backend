package com.rental.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;
import org.apache.tika.Tika;

import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import java.security.GeneralSecurityException;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.FilmDTO;
import com.rental.movie.service.FilmService;
import com.rental.movie.service.SubtitleService;
import com.rental.movie.service.TokenService;
import com.rental.movie.model.dto.RatingRequestDTO;
import com.rental.movie.exception.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api")
public class FilmController {
    @Autowired
    private FilmService filmService;
    @Autowired
    private SubtitleService subtitleService;
    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Lấy danh sách tất cả phim đang hoạt động", description = "Lấy tất cả phim đã active và không xóa mềm") // isActive=true
                                                                                                                                          // &&
                                                                                                                                          // isDeleted=false
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping("/auth/film/active")
    public ResponseEntity<BaseResponse> getAllActivedFilm(@ParameterObject Pageable pageable,
            @RequestParam(defaultValue = "") String search) {
        try {
            BaseResponse response = new BaseResponse("Lấy danh sách thành công", HttpStatus.OK.value(),
                    filmService.getAllActivedFilm(pageable, search));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách tất cả phim không xóa mềm", description = "Lấy tất cả phim không xóa mềm") // isDeleted=false
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping("/film/deleted")
    public ResponseEntity<BaseResponse> getAllNotDeletedFilm(@ParameterObject Pageable pageable,
            @RequestParam(defaultValue = "") String search) {
        try {
            BaseResponse response = new BaseResponse("Lấy danh sách thành công", HttpStatus.OK.value(),
                    filmService.getAllNotDeletedFilm(pageable, search));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách tất cả phim đã xóa mềm", description = "Lấy tất cả phim đã xóa mềm") // isDeleted=true
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping("/film/notDelete")
    public ResponseEntity<BaseResponse> getAllDeletedFilm(@ParameterObject Pageable pageable,
            @RequestParam(defaultValue = "") String search) {
        try {
            BaseResponse response = new BaseResponse("Lấy danh sách thành công", HttpStatus.OK.value(),
                    filmService.getAllDeletedFilm(pageable, search));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Lấy thông tin phim theo ID", description = "Lấy thông tin tổng quan của phim theo ID")
    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công")
    @GetMapping("/auth/film/{id}")
    public ResponseEntity<BaseResponse> getFilmById(@PathVariable String id) {
        try {
            BaseResponse response = new BaseResponse("Lấy thông tin thành công", HttpStatus.OK.value(),
                    filmService.getByIdFilm(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Xem thông tin của phim", description = "Xem thông tin tổng quan của phim theo ID")
    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công")
    @GetMapping("/auth/film/info/{id}")
    public ResponseEntity<BaseResponse> getFilmInfoById(@PathVariable String id) {
        try {
            BaseResponse response = new BaseResponse("Lấy thông tin thành công", HttpStatus.OK.value(),
                    filmService.getFilmInfoById(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy thông tin tài nguyên của phim", description = "Lấy tất cả thông tin tài nguyên của phim theo ID")
    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công")
    @GetMapping("/film/resource/{id}")
    public ResponseEntity<BaseResponse> getFilmResourcesById(@PathVariable String id) {
        try {
            BaseResponse response = new BaseResponse("Lấy thông tin thành công", HttpStatus.OK.value(),
                    filmService.getFilmResourcesById(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Tìm phim theo từ khóa", description = "Tìm tất cả các phim chứa từ khóa tìm kiếm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không có phim"),
            @ApiResponse(responseCode = "200", description = "Tìm thành công")
    })
    @GetMapping("/auth/film/search")
    public ResponseEntity<BaseResponse> searchFilmByName(@RequestParam String keywords) {
        try {
            List<FilmResponseDTO> films = filmService.searchFilmByName(keywords);
            BaseResponse response;

            if (films.isEmpty()) {
                response = new BaseResponse("Không có phim!", HttpStatus.NOT_FOUND.value(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response = new BaseResponse("Tìm thành công", HttpStatus.OK.value(), films);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Thêm phim mới", description = "Thêm phim mới vào hệ thống với trạng thái chưa active") // isActive=false
                                                                                                                         // &&
                                                                                                                         // isDeleted=false
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Thêm thất bại. Có lỗi xảy ra!!!"),
            @ApiResponse(responseCode = "201", description = "Thêm thành công")
    })
    @PostMapping("/film/create")
    public ResponseEntity<BaseResponse> createFilm(@Valid @RequestBody FilmRequestDTO filmDTO) {
        try {
            FilmResponseDTO createdFilm = filmService.createFilm(filmDTO);
            BaseResponse response = new BaseResponse("Thêm thành công", HttpStatus.CREATED.value(), createdFilm);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse("Thêm thất bại. Có lỗi xảy ra: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Xóa mềm phim theo ID", description = "Xóa mềm phim khỏi hệ thống theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Xóa thất bại. Có lỗi xảy ra!!!"),
            @ApiResponse(responseCode = "200", description = "Xóa thành công")
    })
    @DeleteMapping("/film/delete/{id}")
    public ResponseEntity<BaseResponse> deleteFilmById(@PathVariable String id) {
        try {
            filmService.deleteFilmById(id);
            BaseResponse response = new BaseResponse("Xóa thành công", HttpStatus.OK.value(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse("Xóa thất bại. Có lỗi xảy ra: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Cập nhật thông tin phim", description = "Cập nhật thông tin phim theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Cập nhật thất bại. Có lỗi xảy ra!!!"),
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ")
    })
    @PutMapping("/film/update/{id}")
    public ResponseEntity<BaseResponse> updateFilmById(@PathVariable String id,
            @Valid @RequestBody FilmRequestDTO filmDTO) {
        try {
            FilmResponseDTO updatedFilm = filmService.updateFilmById(id, filmDTO);
            BaseResponse response = new BaseResponse("Cập nhật thành công", HttpStatus.OK.value(), updatedFilm);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse("Cập nhật thất bại. Có lỗi xảy ra: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Active phim theo ID", description = "Active trạng thái phim theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Cập nhật thất bại. Có lỗi xảy ra!!!"),
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công")
    })
    @PatchMapping("/film/active/{id}")
    public ResponseEntity<BaseResponse> activeFilm(@PathVariable String id) {
        try {
            FilmResponseDTO updatedFilm = filmService.activeFilm(id);
            BaseResponse response = new BaseResponse("Cập nhật trạng thái thành công", HttpStatus.OK.value(),
                    updatedFilm);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse("Cập nhật trạng thái thất bại. Có lỗi xảy ra: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Deactive phim theo ID", description = "Deactive trạng thái của phim theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Cập nhật thất bại. Có lỗi xảy ra!!!"),
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công")
    })
    @PatchMapping("/film/deactive/{id}")
    public ResponseEntity<BaseResponse> deactiveFilm(@PathVariable String id) {
        try {
            FilmResponseDTO updatedFilm = filmService.deactiveFilm(id);
            BaseResponse response = new BaseResponse("Cập nhật trạng thái thành công", HttpStatus.OK.value(),
                    updatedFilm);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse("Cập nhật trạng thái thất bại. Có lỗi xảy ra: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Khôi phục phim đã xóa mềm", description = "Khôi phục phim đã bị xóa mềm theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Khôi phục thất bại. Có lỗi xảy ra!!!"),
            @ApiResponse(responseCode = "200", description = "Khôi phục thành công")
    })
    @PatchMapping("/film/restore/{id}")
    public ResponseEntity<BaseResponse> restoreFilmById(@PathVariable String id) {
        try {
            FilmResponseDTO restoredFilm = filmService.restoreFilmById(id);
            BaseResponse response = new BaseResponse("Khôi phục thành công", HttpStatus.OK.value(), restoredFilm);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse("Khôi phục thất bại. Có lỗi xảy ra: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Đánh giá phim", description = "Đánh giá phim dựa trên số sao đã chọn")
    @ApiResponse(responseCode = "200", description = "Đánh giá thành công")
    @PostMapping("/film/rate/{filmId}")
    public ResponseEntity<BaseResponse> rateFilm(@PathVariable String filmId,
            @RequestBody RatingRequestDTO ratingRequestDTO) {
        try {
            Double avgRating = filmService.rateFilm(filmId, ratingRequestDTO);
            BaseResponse response = new BaseResponse("Đánh giá thành công", HttpStatus.OK.value(), avgRating);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Lấy điểm đánh giá", description = "Lấy điểm đánh giá của phim")
    @ApiResponse(responseCode = "200", description = "Lấy đánh giá thành công")
    @PostMapping("/film/rating/{filmId}")
    public ResponseEntity<BaseResponse> getRating(@PathVariable String filmId) {
        try {
            Double avgRating = filmService.getRating(filmId);
            BaseResponse response = new BaseResponse("Lấy đánh giá thành công", HttpStatus.OK.value(), avgRating);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Lấy 5 phim hot nhất", description = "Lấy 5 phim có số lượt xem cao nhất")
    @ApiResponse(responseCode = "200", description = "Lấy phim thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy phim")
    @GetMapping("/auth/film/top5")
    public ResponseEntity<BaseResponse> getTop5HotFilm() {
        try {
            List<FilmDTO> top5Films = filmService.getTop5HotFilm();
            BaseResponse response = new BaseResponse("Lấy phim thành công", HttpStatus.OK.value(), top5Films);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Lấy tên thể loại của phim", description = "Lấy danh sách các thể loại của phim hiện tại")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thể loại thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy thể loại hoặc phim")
    @GetMapping("/auth/film/genre/{filmId}")
    public ResponseEntity<BaseResponse> getGenresOfFilm(@PathVariable String filmId) {
        try {
            List<String> genres = filmService.getGenresOfFilm(filmId);
            BaseResponse response = new BaseResponse("Lấy danh sách thể loại thành công", HttpStatus.OK.value(),
                    genres);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse("Không tìm thấy thể loại hoặc phim", HttpStatus.NOT_FOUND.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Lấy danh sách diễn viên của phim", description = "Lấy danh sách các diễn viên của phim hiện tại")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách diễn viên thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy phim")
    @GetMapping("/auth/film/actor/{filmId}")
    public ResponseEntity<BaseResponse> getActorsOfFilm(@PathVariable String filmId) {
        try {
            List<String> actors = filmService.getActorsOfFilm(filmId);
            BaseResponse response = new BaseResponse("Lấy danh sách diễn viên thành công", HttpStatus.OK.value(),
                    actors);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Tăng số lượt xem của phim", description = "Tăng số lượt xem của phim khi người dùng xem phim")
    @ApiResponse(responseCode = "200", description = "Cập nhật số lượt xem thành công")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy phim")
    @PostMapping("/film/view/{filmId}")
    public ResponseEntity<BaseResponse> incrementFilmViews(@PathVariable String filmId) {
        try {
            filmService.incrementViews(filmId);
            BaseResponse response = new BaseResponse("Cập nhật số lượt xem thành công", HttpStatus.OK.value(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Xem phim bước 1", description = "API kiểm tra trước khi xem phim")
    @ApiResponse(responseCode = "200", description = "Kiểm tra thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/file/film/check/{id}")
    public ResponseEntity<BaseResponse> checkUserHasPermissionToAccessFile(@PathVariable String id) {
        filmService.checkUserHasPermissionToAccessFile(id, true);
        BaseResponse response = new BaseResponse("Kiểm tra thành công", HttpStatus.OK.value(), null);
        String accessToken = tokenService.getToken(id);
        response.setData(new HashMap<String, String>() {
            {
                put("AccessToken", accessToken);
            }
        });
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Xem phim bước 2", description = "API xem phim")
    @ApiResponse(responseCode = "200", description = "Tải phim thành công")
    @GetMapping("/auth/file/film/{id}/{token}")
    public ResponseEntity<Resource> getFileFilm(@PathVariable String id, @PathVariable String token)
            throws GeneralSecurityException, IOException {
        tokenService.validateToken(token, id);
        InputStream inputStream = filmService.getFileFilm(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }

    @Operation(summary = "Xem phim bước 2 - Phụ đề", description = "API tải phụ đề")
    @ApiResponse(responseCode = "200", description = "Tải phụ đề thành công")
    @GetMapping("/auth/file/subtitle/{subtitleId}/{token}")
    public ResponseEntity<Resource> getFileSubtitle(@PathVariable String subtitleId, @PathVariable String token)
            throws GeneralSecurityException, IOException {
        tokenService.validateToken(token);
        InputStream inputStream = filmService.getFileSubtitle(subtitleId);

        // Sử dụng Tika để xác định loại MIME
        Tika tika = new Tika();
        String mimeType = tika.detect(inputStream);

        inputStream.reset();

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(new InputStreamResource(inputStream));
    }

    @Operation(summary = "Xem phim bước 2 - Thuyết minh", description = "API tải thuyết minh")
    @ApiResponse(responseCode = "200", description = "Tải thuyết minh thành công")
    @GetMapping("/auth/file/narration/{narrationId}/{token}")
    public ResponseEntity<Resource> getFileNarration(@PathVariable String narrationId, @PathVariable String token)
            throws GeneralSecurityException, IOException {
        tokenService.validateToken(token);
        InputStream inputStream = filmService.getFileNarration(narrationId);

        // Sử dụng Tika để xác định loại MIME
        Tika tika = new Tika();
        String mimeType = tika.detect(inputStream);

        inputStream.reset();

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(new InputStreamResource(inputStream));
    }

}