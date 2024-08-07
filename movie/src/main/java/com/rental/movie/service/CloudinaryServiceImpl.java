package com.rental.movie.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.rental.movie.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file) throws Exception {
        String secureUrl = null;
        log.info("Uploading image to cloudinary");
        // validate file
        String contentType = file.getContentType();
        if (!this.isSupportedContentType(contentType)) {
            log.error("Unsupported content type: " + contentType);
            throw new CustomException("Chỉ hỗ trợ ảnh định dạng PNG, JPG, WEBP", HttpStatus.BAD_REQUEST.value());
        }
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), null);
        secureUrl = (String) result.get("secure_url");
        log.info("Uploaded image to cloudinary with secureUrl: " + secureUrl);
        return secureUrl;
    }

    @Override
    public void delete(String url) throws Exception {
        // Get publicId from url
        String[] parts = url.split("/");
        String publicId = parts[parts.length - 1].split("\\.")[0];
        // Delete image from cloudinary
        log.info("Deleting image with publicId: " + publicId);
        cloudinary.uploader().destroy(publicId, null);
        log.info("Deleted image with publicId: " + publicId);
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg")
                || contentType.equals("image/webp");
    }
}
