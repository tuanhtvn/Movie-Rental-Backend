package com.rental.movie.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    public String upload(MultipartFile file) throws IOException;

    public void delete(String url) throws IOException;
}
