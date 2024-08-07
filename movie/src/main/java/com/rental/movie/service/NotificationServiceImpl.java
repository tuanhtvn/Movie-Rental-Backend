package com.rental.movie.service;

import com.rental.movie.model.dto.NotificationDTO;
import com.rental.movie.util.mapper.NotificationMapper;
import com.rental.movie.model.entity.Notification;
import com.rental.movie.repository.NotificationRepository;
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
    public NotificationDTO getNotificationById(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));
        return notificationMapper.toDTO(notification);
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByUserId(String userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getUnreadNotificationsByUserId(String userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsRead(userId, false);
        return notifications.stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO updateNotification(String id, NotificationDTO notificationDTO) {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));

        existingNotification.setTitle(notificationDTO.getTitle());
        existingNotification.setContent(notificationDTO.getContent());
        return notificationMapper.toDTO(notificationRepository.save(existingNotification));
    }

    @Override
    public void deleteNotification(String id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public void markNotificationAsRead(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void sendNotificationToUser(String userId, NotificationDTO notificationDTO) {
        // Lưu thông báo vào cơ sở dữ liệu
        NotificationDTO savedNotification = createNotification(notificationDTO);

        // Gửi thông báo qua WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, savedNotification);
    }
}