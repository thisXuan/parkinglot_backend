package com.parkinglot_backend.dto;

import lombok.Data;

@Data
public class ForgetPasswordDTO {
    private String phone;
    private String captcha; // 验证码字段
    private String newPassword; // 新密码
}
