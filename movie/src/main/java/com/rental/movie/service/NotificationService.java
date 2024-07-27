package com.rental.movie.service;

import com.rental.movie.model.dto.NotificationDTO;
import java.util.List;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO notificationDTO);
    NotificationDTO getNotificationById(String id);
    List<NotificationDTO> getAllNotifications();
    List<NotificationDTO> getNotificationsByUserId(String userId);
    List<NotificationDTO> getUnreadNotificationsByUserId(String userId);
    NotificationDTO updateNotification(String id, NotificationDTO notificationDTO);
    void deleteNotification(String id);
    void markNotificationAsRead(String id);
    void sendNotificationToUser(String userId, NotificationDTO notificationDTO);
}