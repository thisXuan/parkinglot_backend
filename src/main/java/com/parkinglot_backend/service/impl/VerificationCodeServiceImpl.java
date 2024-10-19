package com.parkinglot_backend.service.impl;

import com.parkinglot_backend.service.VerificationCodeService;
import org.springframework.stereotype.Service;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-10-19
 * @Description: 验证码的实现类，目前暂且默认为123456
 */

@Service // 确保有这个注解
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private static final String CODE = "123456"; // 目前默认的验证码

    @Override
    public String generateCode() {
        return CODE;
    }

    @Override
    public boolean validateCode(String providedCode) {
        return CODE.equals(providedCode);
    }
}