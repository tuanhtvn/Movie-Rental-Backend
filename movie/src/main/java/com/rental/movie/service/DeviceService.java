package com.rental.movie.service;

import java.time.Instant;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.rental.movie.model.dto.DeviceResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface DeviceService {
    public void add(String userId, String token, HttpServletRequest request, Instant expireAt, Instant lastLoggedIn);

    public Page<DeviceResponseDTO> getAll(HttpServletRequest request, Pageable pageable);

    public void delete(String id, HttpServletRequest request);

}
