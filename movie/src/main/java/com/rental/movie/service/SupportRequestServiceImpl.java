package com.rental.movie.service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.model.dto.SupportRequestDTO;
import com.rental.movie.util.mapper.SupportRequestMapper;
import com.rental.movie.model.entity.SupportRequest;
import com.rental.movie.repository.SupportRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportRequestServiceImpl implements SupportRequestService {
    private final SupportRequestRepository supportRequestRepository;
    private final SupportRequestMapper supportRequestMapper;
    private final SimpMessagingTemplate messagingTemplate;


    public SupportRequestServiceImpl(SupportRequestRepository supportRequestRepository,
                                     SupportRequestMapper supportRequestMapper,
                                     SimpMessagingTemplate messagingTemplate) {
        this.supportRequestRepository = supportRequestRepository;
        this.supportRequestMapper = supportRequestMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public SupportRequestDTO createSupportRequest(SupportRequestDTO supportRequestDTO) {
        SupportRequest supportRequest = supportRequestMapper.toEntity(supportRequestDTO);
        supportRequest.setIsResolved(!supportRequest.getIsResolved());
        SupportRequestDTO savedDTO = supportRequestMapper.toDTO(supportRequestRepository.save(supportRequest));
        // send to socket
        messagingTemplate.convertAndSend("/topic/support-requests", savedDTO);

        return savedDTO;
    }

    @Override
    public SupportRequestDTO getSupportRequestById(String id) {
        SupportRequest supportRequest = supportRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu hỗ trợ"));
        return supportRequestMapper.toDTO(supportRequest);
    }

    @Override
    public List<SupportRequestDTO> getAllSupportRequests() {
        List<SupportRequest> supportRequests = supportRequestRepository.findAll();
        return supportRequests.stream()
                .map(supportRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupportRequestDTO> getSupportRequestsByUserId(String userId) {
        List<SupportRequest> supportRequests = supportRequestRepository.findByUserId(userId);
        return supportRequests.stream()
                .map(supportRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupportRequestDTO> getUnresolvedSupportRequests() {
        List<SupportRequest> supportRequests = supportRequestRepository.findByIsResolved(false);
        return supportRequests.stream()
                .map(supportRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSupportRequest(String id) {
        supportRequestRepository.deleteById(id);
    }

}