package com.parkinglot_backend.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String phone;
    private String password;
    private String captcha; // 验证码字段
}
