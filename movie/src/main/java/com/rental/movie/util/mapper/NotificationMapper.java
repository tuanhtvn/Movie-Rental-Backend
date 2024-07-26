package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.NotificationDTO;
import com.rental.movie.model.entity.Notification;
import com.rental.movie.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    @Autowired
    private ModelMapper modelMapper;

    public NotificationDTO toDTO(Notification notification) {
        NotificationDTO notificationDTO = modelMapper.map(notification, NotificationDTO.class);
        if (notification.getUser() != null) {
            notificationDTO.setUserId(notification.getUser().getId());
        }
        return notificationDTO;
    }

    public Notification toEntity(NotificationDTO notificationDTO) {
        Notification notification = modelMapper.map(notificationDTO, Notification.class);
        if (notificationDTO.getUserId() != null) {
            User user = new User();
            user.setId(notificationDTO.getUserId());
            notification.setUser(user);
        }
        return notification;
    }
}
