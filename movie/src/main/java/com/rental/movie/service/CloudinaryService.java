package com.rental.movie.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    public String upload(MultipartFile file, Boolean isImage) throws Exception;

    public void delete(String url) throws Exception;
}
