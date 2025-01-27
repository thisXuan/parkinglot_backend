package com.parkinglot_backend.controller;

import com.parkinglot_backend.dto.ForgetPasswordDTO;
import com.parkinglot_backend.dto.LoginFormDTO;
import com.parkinglot_backend.dto.RegisterDTO;
import com.parkinglot_backend.service.UserService;
import com.parkinglot_backend.service.sign_recordService;
import com.parkinglot_backend.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "账号部分")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private sign_recordService signRecordService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session){
        return userService.login(loginForm,session);
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result logout(HttpSession session){
        return userService.logout(session);
    }

    @Operation(summary = "根据手机号获取用户信息")
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam("phone")String phone){
        return userService.getUserInfo(phone);
    }

    @Operation(summary = "注册账号")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    @Operation(summary = "忘记密码")
    @PostMapping("/reset-password")
    public Result resetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        return userService.resetPassword(forgetPasswordDTO);
    }

    @Operation(summary = "用户签到")
    @PostMapping("/signIn")
    public Result sign(@RequestHeader("token")String token) {
        return signRecordService.sign(token);
    }

    @Operation(summary = "累计签到天数")
    @PostMapping("/getSignInDays")
    public Result getSignInDays(@RequestHeader("token")String token) {
        return signRecordService.getSignInDaysByUserId(token);
    }

}
