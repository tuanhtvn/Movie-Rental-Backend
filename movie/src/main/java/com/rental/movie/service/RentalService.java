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

    /* Start add data for testing */
    @Autowired
    private IAuthentication authManager;
    @Autowired
    private PackageInfoRepository packageInfoRepository;
    @Autowired
    private FilmRepository filmRepository;
    public String addRentalPackageForUser(){
        User user = authManager.getUserAuthentication();
        //tao RentalPackage:
        PackageInfo packageInfo = packageInfoRepository.findById("669777c962c1d84fd296297e").orElse(null);
        if (packageInfo == null)
            return "Package info null";
        RentalPackage rentalPackage = new RentalPackage();
        try {
            rentalPackage.setRegistrationDate(ZonedDateTime.now());
            rentalPackage.setExpirationDate(ZonedDateTime.now());
            rentalPackage.setIsRenewal(true);
            rentalPackage.setPackageInfo(packageInfo);
        } catch (Exception e){
            return "Lỗi trong lúc tạo RentalPackage: " + e.getMessage();
        }
        user.setRentalPackage(rentalPackage);

        //tao rentedFilms
        Film film1 = filmRepository.findById("66911ea4371ec16ae7a0562a").orElse(null);
        Film film2 = filmRepository.findById("66911ea4371ec16ae7a05629").orElse(null);
        RentedFilm rentedFilm1 = new RentedFilm();
        rentedFilm1.setRentalDate(ZonedDateTime.now());
        rentedFilm1.setFilm(film1);
        rentedFilm1.setExpireAt(null);
        rentedFilm1.setExpirationDate(ZonedDateTime.now().plusDays(30));
        RentedFilm rentedFilm2 = new RentedFilm();
        rentedFilm2.setRentalDate(ZonedDateTime.now());
        rentedFilm2.setFilm(film2);
        rentedFilm2.setExpireAt(null);
        rentedFilm2.setExpirationDate(ZonedDateTime.now().plusDays(30));
        List<RentedFilm> list = new ArrayList<>();
        list.add(rentedFilm1);
        list.add(rentedFilm2);
        if (list.isEmpty())
            return "Mảng RentedFilms rỗng";

        user.setRentedFilms(list);

        userRepository.save(user);
        return "Thêm thành công";
    }
    /* End add data for testing */

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
