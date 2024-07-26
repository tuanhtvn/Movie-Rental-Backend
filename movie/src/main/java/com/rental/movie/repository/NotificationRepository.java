package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.rental.movie.model.entity.Notification;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
    List<Notification> findByUserIdAndIsRead(String userId, Boolean isRead);
}