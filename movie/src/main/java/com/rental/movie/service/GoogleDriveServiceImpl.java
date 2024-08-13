package com.rental.movie.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
                log.info("Upload film to Google Drive");
                log.info(file.getOriginalFilename());

                tikaAnalysis.CheckSupportedContentType(file, "video/mp4", "video/quicktime", "video/avi",
                                "video/mkv", "application/x-matroska");

                String folderId = appConfig.getFolderIdFilm();
                return uploadFile(folderId, file);
        }

        @Override
        public String uploadSubtitle(MultipartFile file) throws GeneralSecurityException, IOException {
                log.info("Upload subtitle to Google Drive");
                log.info(file.getOriginalFilename());

                tikaAnalysis.CheckSupportedContentType(file, "text/plain", "text/vtt", "application/x-subrip",
                                "application/x-ass", "application/x-sub");
                String folderId = appConfig.getFolderIdSubtitle();
                return uploadFile(folderId, file);
        }

        @Override
        public String uploadNarration(MultipartFile file) throws GeneralSecurityException, IOException {
                log.info("Upload narration to Google Drive");
                log.info(file.getOriginalFilename());

                tikaAnalysis.CheckSupportedContentType(file, "audio/mpeg", "audio/wav", "video/x-m4v",
                                "audio/x-ms-wma");
                String folderId = appConfig.getFolderIdNarration();
                return uploadFile(folderId, file);
        }

        @Override
        public InputStream getFileAsInputStream(String url) throws GeneralSecurityException, IOException {
                log.info("Get file from Google Drive as InputStream");
                String id = url.substring(url.lastIndexOf("=") + 1);
                return getInstance().files().get(id).executeMediaAsInputStream();
        }

        private String uploadFile(String folderId, MultipartFile file) throws GeneralSecurityException, IOException {
                log.info("Uploading file to Google Drive");
                log.info(file.getOriginalFilename());

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
                log.info("Uploaded file to Google Drive with id: " + uploadFile.getId());
                return "https://drive.google.com/uc?id=" + uploadFile.getId();
        }

        @Override
        public void deleteFile(String url) throws GeneralSecurityException, IOException {
                log.info("Deleting file from Google Drive");
                String id = url.substring(url.lastIndexOf("=") + 1);
                getInstance().files().delete(id).execute();
                log.info("Deleted file from Google Drive with id: " + id);
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
                                .setApplicationName("Movie Rental")
                                .build();

        }
}
