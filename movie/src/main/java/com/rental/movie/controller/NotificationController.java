package com.rental.movie.controller;

import com.rental.movie.model.dto.NotificationDTO;
import com.rental.movie.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    @Operation(summary = "Gửi thông báo cho người dùng",description = "Gửi thông báo cho người dùng")
    @ApiResponse(responseCode = "200", description = "Thông báo đã được gửi thành công")
    @PostMapping("/send")
    @MessageMapping("/send")
    @SendTo("/topic/notification")
    public ResponseEntity<NotificationDTO> sendNotification(@RequestBody NotificationDTO notificationDTO) {
        notificationService.sendNotificationToUser(notificationDTO.getUserId(), notificationDTO);
        return new ResponseEntity<>(notificationDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    @Operation(summary = "Tìm thông báo theo id",description = "Tìm thông báo theo id")
    @ApiResponse(responseCode = "200", description = "Tìm thông báo thành công")
    @GetMapping("/notifications/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable String id) {
        NotificationDTO notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách tất cả thông báo",description = "Lấy tất cả thông báo")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách tất cả thông báo thành công")
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Lấy thông báo theo id người dùng",description = "Lấy thông báo theo id người dùng")
    @ApiResponse(responseCode = "200", description = "Lấy thông báo theo id người dùng thành công")
    @GetMapping("/notifications/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(@PathVariable String userId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Lấy thông báo chưa đọc theo id người dùng",description = "Lấy thông báo chưa đọc theo id người dùng")
    @ApiResponse(responseCode = "200", description = "Lấy thông báo chưa đọc theo id người dùng thành công")
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotificationsByUserId(@PathVariable String userId) {
        List<NotificationDTO> notifications = notificationService.getUnreadNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    @Operation(summary = "Cập nhật thông báo",description = "Cập nhật thông báo")
    @ApiResponse(responseCode = "200", description = "Cập nhật thông báo thành công")
    @PutMapping("/update/{id}")
    public ResponseEntity<NotificationDTO> updateNotification(@PathVariable String id, @RequestBody NotificationDTO notificationDTO) {
        NotificationDTO updatedNotification = notificationService.updateNotification(id, notificationDTO);
        return ResponseEntity.ok(updatedNotification);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE', 'ROLE_USER')")
    @Operation(summary = "Xóa thông báo",description = "Xóa thông báo")
    @ApiResponse(responseCode = "200", description = "Xóa thông báo thành công")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Đánh dấu thông báo đã đọc",description = "Đánh dấu thông báo đã đọc")
    @ApiResponse(responseCode = "200", description = "Đánh dấu thông báo đã đọc thành công")
    @PatchMapping("/mark-read/{id}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable String id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.noContent().build();
    }
}