package com.rental.movie.service;

import java.time.ZonedDateTime;

public interface MailService {
    public void sendMailVerify(String toEmail, String userName, String verifyCode);
    public void notifyAutoRenewal(String toEmail, String userName,
                                  String packageName, ZonedDateTime expirationDate,
                                  long minutesLeft);
}
