package com.rental.movie.model.entity;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RentedFilm {
    @Id
    private String id; // khóa chính
    @DBRef(lazy=true)
    private Film film; // phim
    private ZonedDateTime expirationDate; // ngày hết hạn(30 ngày sau ngày thuê)
    private ZonedDateTime expireAt; // thời gian hết hạn(48h kể từ thời điểm xem phim)
                                    //mặc định null, khi click vào xem phim mới gán giá trị.
    private ZonedDateTime rentalDate; // ngày thuê

    public RentedFilm(){
        this.id = UUID.randomUUID().toString();
    }

    //Hàm tính số phút còn lại của phim thuê
    public long getMinutesLeft(){
        Duration duration;
        ZonedDateTime now = ZonedDateTime.now();

        if (this.expireAt != null && ZonedDateTime.now().isBefore(expireAt)){
            //đã click vào xem phim trước đó
           duration = Duration.between(now, expireAt);
        } else {
            //thời hạn 30 ngày (chưa click xem lần đầu)
            duration = Duration.between(now, expirationDate);
        }

        long minutesLeft = duration.toMinutes();
        return minutesLeft <= 0 ? 0 : minutesLeft;
    }

}
