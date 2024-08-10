package com.rental.movie.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.rental.movie.component.TikaAnalysis;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private TikaAnalysis tikaAnalysis;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String secureUrl = null;
        log.info("Uploading image to cloudinary");
        // validate file
        tikaAnalysis.CheckSupportedContentType(file, "image/png", "image/jpg", "image/jpeg", "image/webp");
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), null);
        secureUrl = (String) result.get("secure_url");
        log.info("Uploaded image to cloudinary with secureUrl: " + secureUrl);
        return secureUrl;
    }

    @Override
    public void delete(String url) throws IOException {
        // Get publicId from url
        String[] parts = url.split("/");
        String publicId = parts[parts.length - 1].split("\\.")[0];
        // Delete image from cloudinary
        log.info("Deleting image with publicId: " + publicId);
        cloudinary.uploader().destroy(publicId, null);
        log.info("Deleted image with publicId: " + publicId);
    }
}
