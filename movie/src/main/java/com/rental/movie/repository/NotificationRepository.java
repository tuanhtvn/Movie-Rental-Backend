package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rental.movie.models.entity.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String>{
    
}
