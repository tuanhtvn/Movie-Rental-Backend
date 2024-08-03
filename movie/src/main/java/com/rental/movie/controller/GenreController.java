package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.GenreRequestDTO;
import com.rental.movie.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class GenreController {
    @Autowired
    private GenreService genreService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách các thể loại có phân trang", description = "Lấy ra tất cả thể loại không bao gồm genre đã xoá mềm")
    @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách các thể loại.")
    @GetMapping("genre/getAll")
    public ResponseEntity<BaseResponse> getAllGenresPage(
            @ParameterObject Pageable pageable,
            @RequestParam(defaultValue = "") String search
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                "Tìm thấy danh sách các thể loại",
                HttpStatus.OK.value(),
                genreService.getAll(pageable, search)));
    }

    @Operation(summary = "Lấy danh sách các thể loại không phân trang", description = "Lấy ra tất cả thể loại đang được active và không bao gồm genre đã xoá mềm")
    @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách các thể loại.")
    @GetMapping("auth/genre/getAllActive")
    public ResponseEntity<BaseResponse> getAllActiveGenresPage(
            @ParameterObject Pageable pageable,
            @RequestParam(defaultValue = "") String search
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                "Tìm thấy danh sách các thể loại",
                HttpStatus.OK.value(),
                genreService.getAllActiveGenres(pageable, search)));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách các thể loại không phân trang", description = "Lấy ra tất cả thể loại đang không active và không bao gồm genre đã xoá mềm")
    @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách các thể loại.")
    @GetMapping("genre/getAllInActive")
    public ResponseEntity<BaseResponse> getAllInActiveGenresPage(
            @ParameterObject Pageable pageable,
            @RequestParam(defaultValue = "") String search
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                "Tìm thấy danh sách các thể loại",
                HttpStatus.OK.value(),
                genreService.getAllInactiveGenres(pageable, search)));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy thể loại đã xoá mềm", description = "Lấy danh sách các thể loại đã xoá mềm")
    @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách thể loại.")
    @GetMapping("genre/getAllSoftDelete")
    public ResponseEntity<BaseResponse> getAllSoftDeletedGenres(
            @ParameterObject Pageable pageable,
            @RequestParam(defaultValue = "") String search
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                "Tìm thấy danh sách thể loại",
                HttpStatus.OK.value(),
                genreService.getAllSoftDeletedGenres(pageable, search)));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy thể loại theo Id", description = "Lấy thể loại theo Id genre")
    @ApiResponse(responseCode = "200", description = "Tìm thấy thể loại.")
    @GetMapping("genre/getOne/{genreId}")
    public ResponseEntity<BaseResponse> getGenreById(@PathVariable String genreId) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                "Tìm thấy thể loại",
                HttpStatus.OK.value(),
                genreService.getGenreById(genreId)));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Tạo mới thể loại", description = "Tạo mới một thể loại")
    @ApiResponse(responseCode = "201", description = "Tạo thể loại mới thành công.")
    @PostMapping("genre/create")
    public ResponseEntity<BaseResponse> createGenre(@Valid @RequestBody GenreRequestDTO genreDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(
                "Tạo thể loại mới thành công.",
                HttpStatus.CREATED.value(),
                genreService.createGenre(genreDTO)));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Cập nhật thể loại", description = "Cập nhật thể loại theo Id thể loại")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thể loại!"),
            @ApiResponse(responseCode = "200", description = "Cập nhật thể loại thành công.")
    })
    @PutMapping("genre/update/{genreId}")
    public ResponseEntity<BaseResponse> updateGenre(
            @PathVariable String genreId,
            @Valid @RequestBody GenreRequestDTO genreDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                "Cập nhật thể loại thành công.",
                HttpStatus.OK.value(),
                genreService.updateGenre(genreId, genreDTO)));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Xoá mềm thể loại", description = "Xoá mềm thể loại theo Id thể loại")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thể loại!"),
            @ApiResponse(responseCode = "200", description = "Xoá mềm thể loại thành công.")
    })
    @PatchMapping("genre/softDelete/{genreId}")
    public ResponseEntity<BaseResponse> softDeleteById(@PathVariable String genreId) {
        genreService.softDeleteGenre(genreId);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                "Xoá mềm thể loại thành công",
                HttpStatus.OK.value(),
                null));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Khôi phục thể loại", description = "Khôi phục thể loại đã xoá mềm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thể loại!"),
            @ApiResponse(responseCode = "200", description = "Khôi phục thể loại thành công.")
    })
    @PatchMapping("genre/restore/{genreId}")
    public ResponseEntity<BaseResponse> restoreById(@PathVariable String genreId) {
        genreService.restoreGenre(genreId);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                "Khôi phục thể loại thành công",
                HttpStatus.OK.value(),
                null));
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
//    @Operation(summary = "Activate thể loại", description = "Activate thể loại theo Id thể loại")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "404", description = "Không tìm thấy thể loại!"),
//            @ApiResponse(responseCode = "200", description = "Activate thể loại thành công.")
//    })
//    @PatchMapping("genre/activate/{genreId}")
//    public ResponseEntity<BaseResponse> activateById(@PathVariable String genreId) {
//        genreService.activateGenre(genreId);
//        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
//                "Activate thể loại thành công",
//                HttpStatus.OK.value(),
//                null));
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
//    @Operation(summary = "Deactivate thể loại", description = "Deactivate thể loại theo Id thể loại")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "404", description = "Không tìm thấy thể loại!"),
//            @ApiResponse(responseCode = "200", description = "Deactivate thể loại thành công.")
//    })
//    @PatchMapping("genre/deactivate/{genreId}")
//    public ResponseEntity<BaseResponse> deactivateById(@PathVariable String genreId) {
//        genreService.deactivateGenre(genreId);
//        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
//                "Khôi phục thể loại thành công",
//                HttpStatus.OK.value(),
//                null));
//    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Active/Deactivate thể loại", description = "Active/Deactivate thể loại theo Id thể loại")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thể loại!"),
            @ApiResponse(responseCode = "200", description = "Active/Deactivate thể loại thành công.")
    })
    @PatchMapping("genre/toggle-active-status/{genreId}")
    public ResponseEntity<BaseResponse> deactivateById(@PathVariable String genreId) {
        boolean result = genreService.toggleActiveStatus(genreId);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                result ? "Active thể loại thành công" : "Deactive thể loại thành công",
                HttpStatus.OK.value(),
                null));
    }
}
