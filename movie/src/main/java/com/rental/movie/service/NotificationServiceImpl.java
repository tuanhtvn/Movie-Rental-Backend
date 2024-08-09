package com.rental.movie.service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.NotificationDTO;
import com.rental.movie.model.dto.NotificationResponseDTO;
import com.rental.movie.model.entity.User;
import com.rental.movie.util.mapper.NotificationMapper;
import com.rental.movie.model.entity.Notification;
import com.rental.movie.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private IAuthentication authentication;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationMapper notificationMapper,
                                   SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification.setIsRead(!notificationDTO.getIsRead());
        return notificationMapper.toDTO(notificationRepository.save(notification));
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        if (notifications.isEmpty()) {
            throw new CustomException("Không có thông báo nào", HttpStatus.NOT_FOUND.value());
        }
        return notificationMapper.notificationResponseDTOSList(notifications);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByUserId(String userId) {
        User currentUser = authentication.getUserAuthentication();
        userId = currentUser.getId();
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        if (notifications.isEmpty()) {
            throw new CustomException("Không có thông báo nào", HttpStatus.NOT_FOUND.value());
        }
        return notificationMapper.notificationResponseDTOSList(notifications);
    }

    @Override
    public List<NotificationResponseDTO> getUnreadNotificationsByUserId(String userId) {
        User currentUser = authentication.getUserAuthentication();
        userId = currentUser.getId();
        List<Notification> notifications = notificationRepository.findByUserIdAndIsRead(userId, false);
        if (notifications.isEmpty()) {
            throw new CustomException("Không có thông báo chưa đọc nào", HttpStatus.NOT_FOUND.value());
        }
        return notificationMapper.notificationResponseDTOSList(notifications);
    }

    @Override
    public NotificationDTO updateNotification(String id, NotificationDTO notificationDTO) {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CustomException("Không tìm thấy thông báo", HttpStatus.NOT_FOUND.value());
                });

        existingNotification.setTitle(notificationDTO.getTitle());
        existingNotification.setContent(notificationDTO.getContent());
        return notificationMapper.toDTO(notificationRepository.save(existingNotification));
    }

    @Override
    public void deleteNotification(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CustomException("Không tìm thấy thông báo", HttpStatus.NOT_FOUND.value());
                });
        notificationRepository.deleteById(id);
    }

    @Override
    public void markNotificationAsRead(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CustomException("Không tìm thấy thông báo", HttpStatus.NOT_FOUND.value());
                });
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void sendNotificationToUser(String userId, NotificationDTO notificationDTO) {
        NotificationDTO savedNotification = createNotification(notificationDTO);
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, savedNotification);
    }
}