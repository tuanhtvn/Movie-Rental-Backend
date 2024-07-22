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

@RestController
@RequestMapping("/api")
public class CloudinaryController {
    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping(path = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadImage(@RequestParam(name = "image", required = true) MultipartFile file)
            throws Exception {
        BaseResponse response = BaseResponse.builder()
                .message("Tải ảnh lên thành công")
                .status(HttpStatus.OK.value())
                .data(cloudinaryService.upload(file, true))
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping({ "/image/delete", "/video/delete" })
    public ResponseEntity<BaseResponse> delete(@RequestParam(name = "url", required = true) String url)
            throws Exception {
        cloudinaryService.delete(url);
        BaseResponse response = BaseResponse.builder()
                .message("Xoá file thành công")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
