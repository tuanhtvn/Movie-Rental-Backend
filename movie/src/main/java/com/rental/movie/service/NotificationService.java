package com.rental.movie.service;

import com.rental.movie.model.dto.NotificationDTO;
import com.rental.movie.model.dto.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO notificationDTO);
    List<NotificationResponseDTO> getAllNotifications();
    List<NotificationResponseDTO> getNotificationsByUserId(String userId);
    List<NotificationResponseDTO> getUnreadNotificationsByUserId(String userId);
    NotificationDTO updateNotification(String id, NotificationDTO notificationDTO);
    void deleteNotification(String id);
    void markNotificationAsRead(String id);
    void sendNotificationToUser(String userId, NotificationDTO notificationDTO);
}