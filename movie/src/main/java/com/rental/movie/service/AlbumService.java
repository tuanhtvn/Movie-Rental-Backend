package com.rental.movie.service;

import com.rental.movie.model.dto.AlbumRequestDTO;
import com.rental.movie.model.dto.AlbumResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlbumService {
    public Page<AlbumResponseDTO> getAllActiveAlbums(Pageable pageable, String search); //isActive=true && isDeleted=false
    public Page<AlbumResponseDTO> getAllInactiveAlbums(Pageable pageable, String search); //isActive=false && isDeleted=false
    public Page<AlbumResponseDTO> getAllSoftDeletedAlbums(Pageable pageable, String search); //isDeleted=true
    public Page<AlbumResponseDTO> getAll(Pageable pageable, String search);
    public AlbumResponseDTO getAlbumById(String albumId);
    public AlbumResponseDTO createAlbum(AlbumRequestDTO albumDTO);
    public AlbumResponseDTO updateAlbum(String albumId, AlbumRequestDTO albumDTO);
    public void softDeleteAlbum(String albumId);
    public void restoreAlbum(String albumId);
    public void activateAlbum(String albumId);
    public void deactivateAlbum(String albumId);
}
