package com.parkinglot_backend.controller;

import com.parkinglot_backend.dto.ForgetPasswordDTO;
import com.parkinglot_backend.dto.LoginFormDTO;
import com.parkinglot_backend.dto.RegisterDTO;
import com.parkinglot_backend.service.UserService;
import com.parkinglot_backend.util.Result;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param loginForm
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session){
        return userService.login(loginForm,session);
    }

    @PostMapping("/logout")
    public Result logout(HttpSession session){
        return userService.logout(session);
    }

    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam("phone")String phone){
        return userService.getUserInfo(phone);
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    @PostMapping("/reset-password")
    public Result resetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        return userService.resetPassword(forgetPasswordDTO);
    }


}
