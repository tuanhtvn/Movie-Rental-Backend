package com.rental.movie.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.rental.movie.component.TikaAnalysis;
import com.rental.movie.config.AppConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleDriveServiceImpl implements GoogleDriveService {

        @Autowired
        private AppConfig appConfig;
        @Autowired
        private TikaAnalysis tikaAnalysis;

        private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        private static final String SERVICE_ACOUNT_KEY_PATH = GenreServiceImpl.class
                        .getResource("/certs/drive/credentials.json").getPath();

        @Override
        public String uploadFilm(MultipartFile file) throws GeneralSecurityException, IOException {
                log.info("Uploading film to Google Drive");
                log.info(file.getOriginalFilename());
                tikaAnalysis.CheckSupportedContentType(file, "video/mp4", "video/quicktime", "video/avi",
                                "video/mkv");
                String folderId = appConfig.getFolderId();

                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(file.getOriginalFilename());

                File uploadFile = getInstance()
                                .files()
                                .create(fileMetadata, new InputStreamContent(
                                                file.getContentType(),
                                                new ByteArrayInputStream(file.getBytes())))
                                .setFields("id").execute();

                log.debug(uploadFile.toString());
                log.info("Uploaded film to Google Drive with id: " + uploadFile.getId());
                return "https://drive.google.com/uc?id=" + uploadFile.getId();
        }

        @Override
        public void deleteFilm(String filmUrl) throws GeneralSecurityException, IOException {
                log.info("Deleting film from Google Drive");
                String id = filmUrl.substring(filmUrl.lastIndexOf("=") + 1);
                getInstance().files().delete(id).execute();
                log.info("Deleted film from Google Drive with id: " + id);
        }

        private static Drive getInstance() throws GeneralSecurityException, IOException {
                GoogleCredentials credentials = GoogleCredentials
                                .fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
                                .createScoped(Collections.singleton(DriveScopes.DRIVE));
                HttpCredentialsAdapter credentialsAdapter = new HttpCredentialsAdapter(credentials);
                return new Drive.Builder(
                                GoogleNetHttpTransport.newTrustedTransport(),
                                JSON_FACTORY,
                                credentialsAdapter)
                                .build();

        }
}
