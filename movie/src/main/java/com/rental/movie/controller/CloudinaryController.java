package com.rental.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.service.CloudinaryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Cloudinary Controller")
public class CloudinaryController {
    @Autowired
    private CloudinaryService cloudinaryService;

    @Operation(summary = "Tải ảnh lên", description = "API tải ảnh lên")
    @ApiResponse(responseCode = "200", description = "Tải ảnh lên thành công")
    @PostMapping(path = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadImage(@RequestParam(name = "image", required = true) MultipartFile file)
            throws Exception {
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
            throws Exception {
        cloudinaryService.delete(url);
        BaseResponse response = BaseResponse.builder()
                .message("Xoá ảnh thành công")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
