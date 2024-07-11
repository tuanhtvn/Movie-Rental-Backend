package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rental.movie.model.entity.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

}
