package com.rental.movie.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.common.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document(collection = "User")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    private String id; // khóa chính
    private String fullName; // họ tên
    private String avatar = "https://res.cloudinary.com/dgpfsipnc/image/upload/v1723042655/rcs1oldxokpbhqbseqxx.jpg"; // ảnh đại diện
    private String email; // email
    private String password; // mật khẩu
    private String passwordUpdate; // mật khẩu cập nhật
    private Boolean isEmailVerified = false; // trạng thái xác thực email
    private AuthProvider authProvider; // nhà cung cấp xác thực
    private Role role; // quyền
    private Verify verify; // thông tin xác thực
    private RentalPackage rentalPackage; // gói thuê
    private List<Item> cart = new ArrayList<>(); // giỏ hàng
    private List<Profile> profiles = new ArrayList<>(); // danh sách hồ sơ
    private List<Device> devices = new ArrayList<>(); // danh sách thiết bị
    private List<RentedFilm> rentedFilms = new ArrayList<>(); // danh sách phim đã thuê
    private List<TransactionHistory> transactionHistories = new ArrayList<>(); // lịch sử giao dịch
    private List<PaymentInfo> paymentInfos = new ArrayList<>(); // thông tin thanh toán
}
