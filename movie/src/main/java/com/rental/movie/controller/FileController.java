package com.rental.movie.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.service.CloudinaryService;
import com.rental.movie.service.GoogleDriveService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "File Controller")
public class FileController {
        @Autowired
        private CloudinaryService cloudinaryService;
        @Autowired
        private GoogleDriveService googleDriveService;

        @Operation(summary = "Tải ảnh lên", description = "API tải ảnh lên. Định dạng ảnh hỗ trợ: jpg, jpeg, png, webp. Kích thước tối đa: 10MB")
        @ApiResponse(responseCode = "200", description = "Tải ảnh lên thành công")
        @PostMapping(path = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<BaseResponse> uploadImage(
                        @RequestParam(name = "image", required = true) MultipartFile file)
                        throws IOException {
                BaseResponse response = BaseResponse.builder()
                                .message("Tải ảnh lên thành công")
                                .status(HttpStatus.OK.value())
                                .data(cloudinaryService.upload(file))
                                .build();
                return ResponseEntity.status(response.getStatus()).body(response);
        }

        @Operation(summary = "Xoá ảnh theo url", description = "API xoá ảnh theo url")
        @ApiResponse(responseCode = "200", description = "Xoá ảnh thành công")
        @DeleteMapping("/image/delete")
        public ResponseEntity<BaseResponse> deleteImage(@RequestParam(name = "url", required = true) String url)
                        throws IOException {
                cloudinaryService.delete(url);
                BaseResponse response = BaseResponse.builder()
                                .message("Xoá ảnh thành công")
                                .status(HttpStatus.OK.value())
                                .build();
                return ResponseEntity.status(response.getStatus()).body(response);
        }

        @Operation(summary = "Tải phim lên", description = "API tải phim lên. Định dạng phim hỗ trợ: mp4, mov, avi, mkv. Kích thước tối đa: 1GB")
        @ApiResponse(responseCode = "200", description = "Tải phim lên thành công")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @PostMapping(path = "/film/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<BaseResponse> uploadFilm(@RequestParam(name = "film", required = true) MultipartFile file)
                        throws GeneralSecurityException, IOException {
                BaseResponse response = BaseResponse.builder()
                                .message("Tải phim lên thành công")
                                .status(HttpStatus.OK.value())
                                .data(googleDriveService.uploadFilm(file))
                                .build();
                return ResponseEntity.status(response.getStatus()).body(response);
        }

        @Operation(summary = "Tải phụ đề lên", description = "API tải phụ đề lên. Định dạng phụ đề hỗ trợ: srt, vtt, ass, sub. Kích thước tối đa: 10MB")
        @ApiResponse(responseCode = "200", description = "Tải phụ đề lên thành công")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @PostMapping(path = "/subtitle/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<BaseResponse> uploadSubtitle(
                        @RequestParam(name = "subtitle", required = true) MultipartFile file)
                        throws GeneralSecurityException, IOException {
                BaseResponse response = BaseResponse.builder()
                                .message("Tải phụ đề lên thành công")
                                .status(HttpStatus.OK.value())
                                .data(googleDriveService.uploadSubtitle(file))
                                .build();
                return ResponseEntity.status(response.getStatus()).body(response);
        }

        @Operation(summary = "Tải file thuyết minh lên", description = "API tải file thuyết minh lên. Định dạng file thuyết minh hỗ trợ: mp3, wav, m4v, wma. Kích thước tối đa: 10MB")
        @ApiResponse(responseCode = "200", description = "Tải thuyết minh lên thành công")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @PostMapping(path = "/narration/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<BaseResponse> uploadNarration(
                        @RequestParam(name = "narration", required = true) MultipartFile file)
                        throws GeneralSecurityException, IOException {
                BaseResponse response = BaseResponse.builder()
                                .message("Tải thuyết minh lên thành công")
                                .status(HttpStatus.OK.value())
                                .data(googleDriveService.uploadNarration(file))
                                .build();
                return ResponseEntity.status(response.getStatus()).body(response);
        }

        @Operation(summary = "Xoá file (film, phụ đề, thuyết minh) theo url", description = "API xoá file theo url")
        @ApiResponse(responseCode = "200", description = "Xoá file thành công")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @DeleteMapping("/file/delete")
        public ResponseEntity<BaseResponse> deleteFile(@RequestParam(name = "url", required = true) String url)
                        throws GeneralSecurityException, IOException {
                googleDriveService.deleteFile(url);
                BaseResponse response = BaseResponse.builder()
                                .message("Xoá file thành công")
                                .status(HttpStatus.OK.value())
                                .build();
                return ResponseEntity.status(response.getStatus()).body(response);
        }
}
