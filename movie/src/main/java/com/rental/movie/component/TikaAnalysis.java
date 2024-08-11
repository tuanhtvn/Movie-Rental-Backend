package com.rental.movie.component;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.rental.movie.exception.CustomException;

@Component
public class TikaAnalysis {
    public void CheckSupportedContentType(MultipartFile file, String... supportedContentTypes) throws IOException {
        String contentType = this.getContentType(file);
        for (String supportedContentType : supportedContentTypes) {
            if (contentType.equals(supportedContentType)) {
                return;
            }
        }
        throw new CustomException("Không hỗ trợ định dạng này: " + contentType, HttpStatus.BAD_REQUEST.value());
    }

    private String getContentType(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        try (InputStream stream = file.getInputStream()) {
            return tika.detect(stream);
        }
    }
}
