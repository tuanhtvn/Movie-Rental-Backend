package com.rental.movie.component;

import com.rental.movie.service.RentalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    private RentalService rentalService;

    // Kiểm tra và gia hạn tự động cho gói thuê (chạy bất đồng bộ mỗi phút)
    @Scheduled(cron = "0 * * * * ?")
    public void checkAndAutoRenewalRentalPackage() {
        try {
            CompletableFuture<Void> future = rentalService.checkAndAutoRenewAllUsers();
            future.get(5, TimeUnit.MINUTES); // Đợi tối đa 5 phút để phương thức hoàn thành
        } catch (TimeoutException e) {
            log.error("Task execution timeout", e);
        } catch (Exception e) {
            log.error("Task execution error", e);
        }
    }

}
