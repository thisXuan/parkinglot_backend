package com.parkinglot_backend.service;

public interface VerificationCodeService {
    String generateCode();
    boolean validateCode(String captcha);

    void removeVerificationCode(String phone);
}
