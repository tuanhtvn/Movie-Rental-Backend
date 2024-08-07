package com.rental.movie.service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.common.PaymentStatus;
import com.rental.movie.common.Role;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.entity.*;
import com.rental.movie.repository.InvoiceRepository;
import com.rental.movie.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
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
                processUserForRenewal(user);
            }
            pageNumber++;
        } while (usersPage.hasNext());

        return CompletableFuture.completedFuture(null); // Trả về một CompletableFuture hoàn thành ngay lập tức
    }

    private void processUserForRenewal(User user) {
        if (user.getRole() == Role.USER) {
            RentalPackage rentalPackage = user.getRentalPackage();
            if (rentalPackage != null && rentalPackage.getIsRenewal() && rentalPackage.getMinutesLeft() == 0) {
                // kiểm tra coi gói đó có còn hoạt động không
                if (!rentalPackage.getPackageInfo().getIsActive() || rentalPackage.getPackageInfo().getIsDeleted()) {
                    handleUnavailableRentalPackage(user, rentalPackage);
                } else {
                    try {
                        autoRenewal(user, rentalPackage);
                    } catch (Exception e) {
                        log.error("Lỗi khi gia hạn gói thuê cho người dùng: " + user.getFullName(), e);
                    }
                }
            } else {
                log.info("Người dùng: {} không cần gia hạn gói thuê.", user.getFullName());
            }
        } else {
            log.info("Người dùng: {} không có vai trò USER, bỏ qua.", user.getFullName());
        }
    }

    private void handleUnavailableRentalPackage(User user, RentalPackage rentalPackage) {
        rentalPackage.setIsRenewal(false);
        user.setRentalPackage(rentalPackage);
        userRepository.save(user);

        log.info("Gói thuê này đã hết hạn và không còn hỗ trợ, đã huỷ gia hạn gói thuê cho: {} vào lúc {}",
                user.getFullName(), ZonedDateTime.now());
        // Gửi email thông báo
        notifyCancelAutoRenewalAsync(user.getEmail(), user.getFullName(),
                rentalPackage.getPackageInfo().getPackageName());
    }

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

        // Gửi email thông báo
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

    @Async
    public void notifyCancelAutoRenewalAsync(String toEmail, String userName, String pktName) {
        mailService.notifyCancelAutoRenewal(toEmail, userName, pktName);
    }

    //Kiểm tra xem có phải là lần click đầu tiên vào phim thuê không:
    public boolean checkFirstClickOnRentedFilm(RentedFilm rentedFilm) {
        return rentedFilm.getExpireAt() == null;
    }

    public List<RentedFilm> getRentedFilmsByCurrentUser() {
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

    // Thêm danh sách phim vào danh sách phim thuê của User

    public void addRentedFilm(List<Film> filmList, User user){
        // Lặp qua danh sách các bộ phim
        for (Film film : filmList) {
            // Tạo một đối tượng RentedFilm mới cho mỗi bộ phim
            RentedFilm rentedFilm = new RentedFilm();
            rentedFilm.setId(new ObjectId().toString());
            rentedFilm.setFilm(film); // Gán bộ phim vào RentedFilm
            rentedFilm.setRentalDate(ZonedDateTime.now()); // Gán ngày thuê là thời điểm hiện tại
            rentedFilm.setExpirationDate(ZonedDateTime.now().plusDays(30)); // Gán ngày hết hạn là 30 ngày sau
            // Thêm RentedFilm vào danh sách phim thuê của người dùng
            user.getRentedFilms().add(rentedFilm);
        }
    }

    // Thêm gói thuê vào User
    public void addRentalPackage(PackageInfo packageInfo, User user) {
        // Tạo một đối tượng RentalPackage mới
        RentalPackage rentalPackage = new RentalPackage();
        // Gán thông tin gói thuê
        rentalPackage.setPackageInfo(packageInfo);
        rentalPackage.setRegistrationDate(ZonedDateTime.now());
        rentalPackage.setExpirationDate(ZonedDateTime.now().plusDays(packageInfo.getTimeDuration()));
        rentalPackage.setIsRenewal(true);

        // Thêm gói thuê vào danh sách gói thuê của người dùng
        user.setRentalPackage(rentalPackage);
    }
}