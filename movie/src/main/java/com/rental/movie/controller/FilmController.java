package com.rental.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.entity.Film;
import com.rental.movie.service.FilmService;
import com.rental.movie.exception.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/")
public class FilmController {
     @Autowired
     private FilmService filmService;

     @PreAuthorize("hasRole('ROLE_USER')")
     @Operation(summary = "Lấy danh sách tất cả phim đang hoạt động", description = "Lấy tất cả phim đã active và không xóa mềm") //isActive=true && isDeleted=false
     @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
     @GetMapping("/films")
     public ResponseEntity<BaseResponse> getAllActivedFilm(Pageable pageable, String search) {
         try{
             BaseResponse response = new BaseResponse("Lấy danh sách thành công", HttpStatus.OK.value(), filmService.getAllActivedFilm(pageable, search));
             return new ResponseEntity<>(response, HttpStatus.OK);
         } catch (CustomException e) {
             BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
         }
    }

     @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
     @Operation(summary = "Lấy danh sách tất cả phim không xóa mềm", description = "Lấy tất cả phim không xóa mềm") // isDeleted=false
     @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
     @GetMapping("/films/deleted")
     public ResponseEntity<BaseResponse> getAllNotDeletedFilm(Pageable pageable, String search) {
         try {
             BaseResponse response = new BaseResponse("Lấy danh sách thành công", HttpStatus.OK.value(), filmService.getAllNotDeletedFilm(pageable, search));
             return new ResponseEntity<>(response, HttpStatus.OK);
         } catch (CustomException e) {
             BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
         }
     }

     @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
     @Operation(summary = "Lấy danh sách tất cả phim đã xóa mềm", description = "Lấy tất cả phim đã xóa mềm") // isDeleted=true
     @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
     @GetMapping("/films/notdelete")
     public ResponseEntity<BaseResponse> getAllDeletedFilm(Pageable pageable, String search) {
         try {
             BaseResponse response = new BaseResponse("Lấy danh sách thành công", HttpStatus.OK.value(), filmService.getAllDeletedFilm(pageable, search));
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
     @GetMapping("/search")
     public ResponseEntity<BaseResponse> searchFilmByName(@RequestParam String keywords) {
         try {
             List<Film> films = filmService.searchFilmByName(keywords);
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
     @Operation(summary = "Thêm phim mới", description = "Thêm phim mới vào hệ thống với trạng thái chưa active") // isActive=false && isDeleted=false
     @ApiResponses(value = {
             @ApiResponse(responseCode = "404", description = "Thêm thất bại. Có lỗi xảy ra!!!"),
             @ApiResponse(responseCode = "201", description = "Thêm thành công")
     })
     @PostMapping("/create")
     public ResponseEntity<BaseResponse> createFilm(@Valid @RequestBody FilmRequestDTO filmDTO) {
         try {
             FilmResponseDTO createdFilm = filmService.createFilm(filmDTO);
             BaseResponse response = new BaseResponse("Thêm thành công", HttpStatus.CREATED.value(), createdFilm);
             return new ResponseEntity<>(response, HttpStatus.CREATED);
         } catch (Exception e) {
             BaseResponse response = new BaseResponse("Thêm thất bại. Có lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
             return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }

     @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
     @Operation(summary = "Xóa mềm phim theo ID", description = "Xóa mềm phim khỏi hệ thống theo ID")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "404", description = "Xóa thất bại. Có lỗi xảy ra!!!"),
             @ApiResponse(responseCode = "200", description = "Xóa thành công")
     })
     @DeleteMapping("/delete/{id}")
     public ResponseEntity<BaseResponse> deleteFilmById(@PathVariable String id) {
         try {
             filmService.deleteFilmById(id);
             BaseResponse response = new BaseResponse("Xóa thành công", HttpStatus.OK.value(), null);
             return new ResponseEntity<>(response, HttpStatus.OK);
         } catch (CustomException e) {
             BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
         } catch (Exception e) {
             BaseResponse response = new BaseResponse("Xóa thất bại. Có lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
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
     @PutMapping("/update/{id}")
     public ResponseEntity<BaseResponse> updateFilmById(@PathVariable String id, @Valid @RequestBody FilmRequestDTO filmDTO) {
         try {
             FilmResponseDTO updatedFilm = filmService.updateFilmById(id, filmDTO);
             BaseResponse response = new BaseResponse("Cập nhật thành công", HttpStatus.OK.value(), updatedFilm);
             return new ResponseEntity<>(response, HttpStatus.OK);
         } catch (CustomException e) {
             BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
         } catch (Exception e) {
             BaseResponse response = new BaseResponse("Cập nhật thất bại. Có lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
             return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }

     @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
     @Operation(summary = "Active/Deactive phim theo ID", description = "Thay đổi trạng thái isActive của phim theo ID")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "404", description = "Cập nhật thất bại. Có lỗi xảy ra!!!"),
             @ApiResponse(responseCode = "200", description = "Cập nhật thành công")
     })
     @PatchMapping("/active_deactive/{id}")
     public ResponseEntity<BaseResponse> changeStatusFilm(@PathVariable String id) {
         try {
             FilmResponseDTO updatedFilm = filmService.changeStatusFilm(id);
             BaseResponse response = new BaseResponse("Cập nhật trạng thái thành công", HttpStatus.OK.value(), updatedFilm);
             return new ResponseEntity<>(response, HttpStatus.OK);
         } catch (CustomException e) {
             BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
         } catch (Exception e) {
             BaseResponse response = new BaseResponse("Cập nhật trạng thái thất bại. Có lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
             return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }

     @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
     @Operation(summary = "Khôi phục phim đã xóa mềm", description = "Khôi phục phim đã bị xóa mềm theo ID")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "404", description = "Khôi phục thất bại. Có lỗi xảy ra!!!"),
             @ApiResponse(responseCode = "200", description = "Khôi phục thành công")
     })
     @PatchMapping("/restore/{id}")
     public ResponseEntity<BaseResponse> restoreFilmById(@PathVariable String id) {
         try {
             FilmResponseDTO restoredFilm = filmService.restoreFilmById(id);
             BaseResponse response = new BaseResponse("Khôi phục thành công", HttpStatus.OK.value(), restoredFilm);
             return new ResponseEntity<>(response, HttpStatus.OK);
         } catch (CustomException e) {
             BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
         } catch (Exception e) {
             BaseResponse response = new BaseResponse("Khôi phục thất bại. Có lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
             return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }

}
