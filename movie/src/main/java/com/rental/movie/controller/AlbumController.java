package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.AlbumRequestDTO;
import com.rental.movie.service.AlbumService;
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
public class AlbumController {
        @Autowired
        private AlbumService albumService;

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Lấy danh sách các album có phân trang", description = "Lấy ra tất cả album không bao gồm album đã xoá mềm")
        @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách các album.")
        @GetMapping("album/getAll")
        public ResponseEntity<BaseResponse> getAllAlbumsPage(
                @ParameterObject Pageable pageable,
                @RequestParam(defaultValue = "") String search
        ) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                        "Tìm thấy danh sách các album",
                        HttpStatus.OK.value(),
                        albumService.getAll(pageable, search)));
        }

        @Operation(summary = "Lấy danh sách các album không phân trang", description = "Lấy ra tất cả album đang được active và không bao gồm album đã xoá mềm")
        @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách các album.")
        @GetMapping("auth/album/getAllActive")
        public ResponseEntity<BaseResponse> getAllActiveAlbumsPage(
                @ParameterObject Pageable pageable,
                @RequestParam(defaultValue = "") String search
        ) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                        "Tìm thấy danh sách các album",
                        HttpStatus.OK.value(),
                        albumService.getAllActiveAlbums(pageable, search)));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Lấy danh sách các album không phân trang", description = "Lấy ra tất cả album đang không active và không bao gồm album đã xoá mềm")
        @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách các album.")
        @GetMapping("album/getAllInActive")
        public ResponseEntity<BaseResponse> getAllInActiveAlbumsPage(
                @ParameterObject Pageable pageable,
                @RequestParam(defaultValue = "") String search
        ) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                        "Tìm thấy danh sách các album",
                        HttpStatus.OK.value(),
                        albumService.getAllInactiveAlbums(pageable, search)));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Lấy album đã xoá mềm", description = "Lấy danh sách các album đã xoá mềm")
        @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách album.")
        @GetMapping("album/getAllSoftDelete")
        public ResponseEntity<BaseResponse> getAllSoftDeletedAlbums(
                @ParameterObject Pageable pageable,
                @RequestParam(defaultValue = "") String search
        ) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                        "Tìm thấy danh sách album",
                        HttpStatus.OK.value(),
                        albumService.getAllSoftDeletedAlbums(pageable, search)));
        }

        @Operation(summary = "Lấy album theo Id", description = "Lấy album theo Id album")
        @ApiResponse(responseCode = "200", description = "Tìm thấy album.")
        @GetMapping("auth/album/getOne/{albumId}")
        public ResponseEntity<BaseResponse> getAlbumById(@PathVariable String albumId) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                        "Tìm thấy album",
                        HttpStatus.OK.value(),
                        albumService.getAlbumById(albumId)));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Tạo mới album", description = "Tạo mới một album")
        @ApiResponse(responseCode = "201", description = "Tạo album mới thành công.")
        @PostMapping("album/create")
        public ResponseEntity<BaseResponse> createAlbum(@Valid @RequestBody AlbumRequestDTO albumDTO) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(
                    "Tạo album mới thành công.",
                    HttpStatus.CREATED.value(),
                    albumService.createAlbum(albumDTO)));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Cập nhật album", description = "Cập nhật album theo Id album")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "404", description = "Không tìm thấy album!"),
                @ApiResponse(responseCode = "200", description = "Cập nhật album thành công.")
        })
        @PutMapping("album/update/{albumId}")
        public ResponseEntity<BaseResponse> updateAlbum(
                @PathVariable String albumId,
                @Valid @RequestBody AlbumRequestDTO albumDTO) {
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                    "Cập nhật album thành công.",
                    HttpStatus.OK.value(),
                    albumService.updateAlbum(albumId, albumDTO)));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Xoá mềm album", description = "Xoá mềm album theo Id album")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "404", description = "Không tìm thấy album!"),
                @ApiResponse(responseCode = "200", description = "Xoá mềm album thành công.")
        })
        @PatchMapping("album/softDelete/{albumId}")
        public ResponseEntity<BaseResponse> softDeleteById(@PathVariable String albumId) {
            albumService.softDeleteAlbum(albumId);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                    "Xoá mềm album thành công",
                    HttpStatus.OK.value(),
                    null));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Khôi phục album", description = "Khôi phục album đã xoá mềm")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "404", description = "Không tìm thấy album!"),
                @ApiResponse(responseCode = "200", description = "Khôi phục album thành công.")
        })
        @PatchMapping("album/restore/{albumId}")
        public ResponseEntity<BaseResponse> restoreById(@PathVariable String albumId) {
            albumService.restoreAlbum(albumId);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                    "Khôi phục album thành công",
                    HttpStatus.OK.value(),
                    null));
        }

//        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
//        @Operation(summary = "Activate album", description = "Activate album theo Id album")
//        @ApiResponses(value = {
//                @ApiResponse(responseCode = "404", description = "Không tìm thấy album!"),
//                @ApiResponse(responseCode = "200", description = "Activate album thành công.")
//        })
//        @PatchMapping("album/activate/{albumId}")
//        public ResponseEntity<BaseResponse> activateById(@PathVariable String albumId) {
//            albumService.activateAlbum(albumId);
//            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
//                    "Activate album thành công",
//                    HttpStatus.OK.value(),
//                    null));
//        }
//
//        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
//        @Operation(summary = "Deactivate album", description = "Deactivate album theo Id danh mục")
//        @ApiResponses(value = {
//                @ApiResponse(responseCode = "404", description = "Không tìm thấy album!"),
//                @ApiResponse(responseCode = "200", description = "Deactivate album thành công.")
//        })
//        @PatchMapping("album/deactivate/{albumId}")
//        public ResponseEntity<BaseResponse> deactivateById(@PathVariable String albumId) {
//            albumService.deactivateAlbum(albumId);
//            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
//                    "Khôi phục album thành công",
//                    HttpStatus.OK.value(),
//                    null));
//        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Active/Deactivate album", description = "Active/Deactivate album theo Id danh mục")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "404", description = "Không tìm thấy album!"),
                @ApiResponse(responseCode = "200", description = "Active/Deactivate album thành công.")
        })
        @PatchMapping("album/toggle-active-status/{albumId}")
        public ResponseEntity<BaseResponse> deactivateById(@PathVariable String albumId) {
            boolean result = albumService.toggleActiveStatus(albumId);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                    result ? "Active album thành công" : "Deactive album thành công",
                    HttpStatus.OK.value(),
                    null));
        }
}
