package com.rental.movie.service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.common.PaymentStatus;
import com.rental.movie.common.Role;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.entity.*;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.repository.InvoiceRepository;
import com.rental.movie.repository.PackageInfoRepository;
import com.rental.movie.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class RentalService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private IAuthentication authManager;

    @Transactional
    @Async
    public CompletableFuture<Void> checkAndAutoRenewAllUsers() {
        int pageSize = 50; // Số lượng người dùng mỗi trang
        int pageNumber = 0;
        Page<User> usersPage;

        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            usersPage = userRepository.findAll(pageable);
            List<User> users = usersPage.getContent();

            log.info("Bắt đầu kiểm tra gia hạn tự động gói thuê cho người dùng. Trang: {}, Số lượng: {}", pageNumber, users.size());
            for (User user : users) {
                // Chỉ kiểm tra nếu là USER
                if (user.getRole() == Role.USER) {
                    RentalPackage rentalPackage = user.getRentalPackage();
                    if (rentalPackage != null && rentalPackage.getIsRenewal() && rentalPackage.getMinutesLeft() == 0) {
                        try {
                            autoRenewal(user, rentalPackage);
                        } catch (Exception e) {
                            log.error("Lỗi khi gia hạn gói thuê cho người dùng: " + user.getFullName(), e);
                        }
                    } else {
                        log.info("Người dùng: {} không cần gia hạn gói thuê.", user.getFullName());
                    }
                } else {
                    log.info("Người dùng: {} không có vai trò USER, bỏ qua.", user.getFullName());
                }
            }
            pageNumber++;
        } while (usersPage.hasNext());

        return CompletableFuture.completedFuture(null); // Trả về một CompletableFuture hoàn thành ngay lập tức
    }


    @Transactional
    public void autoRenewal(User user, RentalPackage rentalPackage) {
        rentalPackage.setExpirationDate(ZonedDateTime.now().plusDays(rentalPackage.getPackageInfo().getTimeDuration()));
        Invoice invoice = new Invoice("fake id",
                ZonedDateTime.now(),
                PaymentStatus.PENDING,
                rentalPackage.getPackageInfo().getPrice(),
                null,
                rentalPackage.getPackageInfo(),
                user
        );
        invoiceRepository.save(invoice);
        user.setRentalPackage(rentalPackage);
        userRepository.save(user);

        // Gửi email thông báo qua hàng đợi
        notifyAutoRenewalAsync(user.getEmail(), user.getFullName(),
                rentalPackage.getPackageInfo().getPackageName(),
                rentalPackage.getExpirationDate(), rentalPackage.getMinutesLeft());
        log.info("Đã gia hạn gói thuê thành công cho: {} vào lúc {}", user.getFullName(), ZonedDateTime.now());
    }

    @Async
    public void notifyAutoRenewalAsync(String toEmail, String userName,
                                       String packageName, ZonedDateTime expirationDate, long minutesLeft) {
        mailService.notifyAutoRenewal(toEmail, userName, packageName, expirationDate, minutesLeft);
    }

    //Kiểm tra xem có phải là lần click đầu tiên vào phim thuê không:
    public boolean checkFirstClickOnRentedFilm(RentedFilm rentedFilm) {
        return rentedFilm.getExpireAt() == null;
    }

    public List<RentedFilm> getRentedFilmsByCurrentUser(){
        User user = authManager.getUserAuthentication();
        if (user == null) {
            throw new CustomException("Không tìm thấy người dùng!", HttpStatus.NOT_FOUND.value());
        }

        List<RentedFilm> rentedFilms = user.getRentedFilms();
        if (rentedFilms.isEmpty()) {
            throw new CustomException("Không tìm thấy phim thuê!", HttpStatus.NOT_FOUND.value());
        }
        return rentedFilms;
    }

}
