package com.rental.movie.service;

import com.rental.movie.model.dto.SupportRequestDTO;
import com.rental.movie.model.dto.SupportRequestResponseDTO;

import java.util.List;

public interface SupportRequestService {
    SupportRequestDTO createSupportRequest(SupportRequestDTO supportRequestDTO);
    List<SupportRequestResponseDTO> getAllSupportRequests();
    List<SupportRequestResponseDTO> getSupportRequestsByUserId();
    List<SupportRequestResponseDTO> getUnresolvedSupportRequests();
    void deleteSupportRequest(String id);
}