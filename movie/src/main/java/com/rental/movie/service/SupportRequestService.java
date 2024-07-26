package com.rental.movie.service;

import com.rental.movie.model.dto.SupportRequestDTO;
import java.util.List;

public interface SupportRequestService {
    SupportRequestDTO createSupportRequest(SupportRequestDTO supportRequestDTO);
    SupportRequestDTO getSupportRequestById(String id);
    List<SupportRequestDTO> getAllSupportRequests();
    List<SupportRequestDTO> getSupportRequestsByUserId(String userId);
    List<SupportRequestDTO> getUnresolvedSupportRequests();
    void deleteSupportRequest(String id);
}