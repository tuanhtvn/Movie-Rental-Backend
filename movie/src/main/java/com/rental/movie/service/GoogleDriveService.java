package com.rental.movie.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.web.multipart.MultipartFile;

public interface GoogleDriveService {
    public String uploadFilm(MultipartFile file) throws GeneralSecurityException, IOException;

    public void deleteFilm(String filmUrl) throws GeneralSecurityException, IOException;
}
