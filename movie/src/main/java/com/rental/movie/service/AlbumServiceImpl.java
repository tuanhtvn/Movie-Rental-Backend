package com.rental.movie.service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.common.Role;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.AlbumRequestDTO;
import com.rental.movie.model.dto.AlbumResponseDTO;
import com.rental.movie.model.entity.Album;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.AlbumRepository;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.util.mapper.AlbumMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private IAuthentication authManager;

    @Override
    public Page<AlbumResponseDTO> getAll(Pageable pageable, String search) {
        Page<Album> albums = albumRepository.findAll(pageable, search);
        log.info("Get all albums: {}", albums.getContent().toString());
        if(albums.isEmpty()) {
            log.error("No albums found");
            throw new CustomException("Không có album nào", HttpStatus.NOT_FOUND.value());
        }
        return albumMapper.convertToDTO(albums);
    }

    @Override
    public Page<AlbumResponseDTO> getAllActiveAlbums(Pageable pageable, String search) {
        Page<Album> albums = albumRepository.findAllActive(pageable, search);
        log.info("Get all active albums: {}", albums.getContent().toString());
        if(albums.isEmpty()){
            log.error("No active albums found");
            throw new CustomException("Không có album nào", HttpStatus.NOT_FOUND.value());
        }
        return albumMapper.convertToDTO(albums);
    }

    @Override
    public Page<AlbumResponseDTO> getAllInactiveAlbums(Pageable pageable, String search) {
        Page<Album> albums = albumRepository.findAllInActive(pageable, search);
        log.info("Get all inactive albums: {}", albums.getContent().toString());
        if(albums.isEmpty()){
            log.error("No inactive albums found");
            throw new CustomException("Không có album nào", HttpStatus.NOT_FOUND.value());
        }
        return albumMapper.convertToDTO(albums);
    }

    @Override
    public Page<AlbumResponseDTO> getAllSoftDeletedAlbums(Pageable pageable, String search) {
        Page<Album> albums = albumRepository.findAllSoftDelete(pageable, search);
        log.info("Get all soft delete albums: {}", albums.getContent().toString());
        if(albums.isEmpty()){
            log.error("No soft delete albums found");
            throw new CustomException("Không tìm thấy albums nào", HttpStatus.NOT_FOUND.value());
        }
        return albumMapper.convertToDTO(albums);
    }

    @Override
    public AlbumResponseDTO getAlbumById(String AlbumId) {
        User user = authManager.getUserAuthentication();
        Album album = albumRepository.findById(AlbumId)
                .orElseThrow(() -> {
                    log.error("Album not found");
                    return new CustomException("Không tìm thấy album", HttpStatus.NOT_FOUND.value());
                });
        if(user.getRole().equals(Role.USER) && album.getIsActive().equals(false)) {
            log.error("User get inactive album");
            throw new CustomException("Bạn không có quyền truy cập album", HttpStatus.METHOD_NOT_ALLOWED.value());
        }
        log.info("Get album by id {}", album.toString());
        return albumMapper.convertToDTO(album);
    }

    @Override
    @Transactional
    public AlbumResponseDTO createAlbum(AlbumRequestDTO AlbumDTO) {
        Album album = albumMapper.convertToEntity(AlbumDTO);
        log.info("Create album {}", album.toString());
        return albumMapper.convertToDTO(albumRepository.save(album));
    }

    @Override
    @Transactional
    public AlbumResponseDTO updateAlbum(
            String AlbumId,
            AlbumRequestDTO AlbumDTO
    ) {
        Album album = albumRepository.findById(AlbumId)
                .orElseThrow(() -> {
                    log.error("Album not found");
                    return new CustomException("Album không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        boolean anyFilmNotFound = AlbumDTO.getFilmId().stream().anyMatch(
                (filmId) -> !filmRepository.existsById(filmId)
        );
        if(anyFilmNotFound) {
            log.error("Film not found");
            throw new CustomException("Phim không tồn tại", HttpStatus.NOT_FOUND.value());
        }
        log.info("Update album {}", album.toString());
        album = albumMapper.convertToEntity(AlbumDTO, album);
        return albumMapper.convertToDTO(albumRepository.save(album));
    }

    @Override
    @Transactional
    public void softDeleteAlbum(String AlbumId) {
        Album album = albumRepository.findById(AlbumId)
                .orElseThrow(() -> {
                    log.error("Album not found");
                    return new CustomException("ID Album không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        album.setIsDeleted(true);
        albumRepository.save(album);
        log.info("Soft delete album {}", album.toString());
    }

    @Override
    @Transactional
    public void restoreAlbum(String AlbumId) {
        Album album = albumRepository.findById(AlbumId)
                .orElseThrow(() -> {
                    log.error("Album not found");
                    return new CustomException("ID Album không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        album.setIsDeleted(false);
        albumRepository.save(album);
        log.info("Restore album {}", album.toString());
    }

//    @Override
//    @Transactional
//    public void activateAlbum(String AlbumId) {
//        Album album = albumRepository.findById(AlbumId)
//                .orElseThrow(() -> {
//                    log.error("Album not found");
//                    return new CustomException("ID Album không tồn tại", HttpStatus.NOT_FOUND.value());
//                });
//        album.setIsActive(true);
//        albumRepository.save(album);
//        log.info("Activate album {}", album.toString());
//    }
//
//    @Override
//    @Transactional
//    public void deactivateAlbum(String AlbumId) {
//        Album album = albumRepository.findById(AlbumId)
//                .orElseThrow(() -> {
//                    log.error("Album not found");
//                    return new CustomException("ID Album không tồn tại", HttpStatus.NOT_FOUND.value());
//                });
//        album.setIsActive(false);
//        albumRepository.save(album);
//        log.info("Deactivate album {}", album.toString());
//    }

    @Override
    @Transactional
    public boolean toggleActiveStatus(String AlbumId) {
        Album album = albumRepository.findById(AlbumId)
                .orElseThrow(() -> {
                    log.error("Album not found");
                    return new CustomException("ID Album không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        album.setIsActive(!album.getIsActive());
        albumRepository.save(album);
        log.info("Deactivate album {}", album.toString());
        return album.getIsActive();
    }
}
