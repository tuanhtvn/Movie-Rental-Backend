package com.rental.movie.service;

public interface MailService {
    public void sendMailVerify(String toEmail, String userName, String verifyCode);
}
