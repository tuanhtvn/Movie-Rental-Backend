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
    private String id;
    private String fullName;
    private String email;
    private String password;
    private Boolean isEmailVerified = false;
    private AuthProvider authProvider;
    private Role role;
    private Verify verify;
    private RentalPackage rentalPackage;
    private List<Item> cart = new ArrayList<>();
    private List<Profile> profiles = new ArrayList<>();
    private List<Device> devices = new ArrayList<>();
    private List<RentedFilm> rentedFilms = new ArrayList<>();
    private List<TransactionHistory> transactionHistories = new ArrayList<>();
    private List<PaymentInfo> paymentInfos = new ArrayList<>();
}
