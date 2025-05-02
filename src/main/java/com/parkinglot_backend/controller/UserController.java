package com.parkinglot_backend.controller;

import com.parkinglot_backend.dto.ForgetPasswordDTO;
import com.parkinglot_backend.dto.LoginFormDTO;
import com.parkinglot_backend.dto.RegisterDTO;
import com.parkinglot_backend.dto.ReviewDTO;
import com.parkinglot_backend.entity.User;
import com.parkinglot_backend.service.CouponService;
import com.parkinglot_backend.service.ReviewService;
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

    @Autowired
    private CouponService couponService;

    @Autowired
    private ReviewService reviewService;

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

    @Operation(summary = "获取用户信息")
    @GetMapping("/getUserRole")
    public Result getUserRole(@RequestHeader("token")String token){
        return userService.getUserRole(token);
    }

    @Operation(summary = "返回用户信息")
    @GetMapping("/getUsers")
    public Result getUsers(@RequestHeader("token")String token){
        return userService.getUsers(token);
    }

    @Operation(summary = "修改用户信息")
    @PostMapping("/updateUser")
    public Result updateUsers(@RequestHeader("token")String token,@RequestBody User user){
        return userService.updateUsers(token,user);
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
    @GetMapping("/getSignInDays")
    public Result getSignInDays(@RequestHeader("token")String token) {
        return signRecordService.getSignInDaysByUserId(token);
    }

    @Operation(summary = "获取成长值")
    @GetMapping("/getPoint")
    public Result getPoint(@RequestHeader("token")String token) {
        return signRecordService.getPoint(token);
    }

    @Operation(summary = "查看优惠券成长值")
    @GetMapping("/getCoupon")
    public Result getCoupon(@RequestParam(value = "type", required = false) String type){
        return couponService.getCoupon(type);
    }

    @Operation(summary = "兑换优惠券")
    @PostMapping("/exchangeCoupon")
    public Result exchangeCoupon(@RequestHeader("token")String token,@RequestParam("id")Integer id){
        return couponService.exchangeCoupon(token,id);
    }

    @Operation(summary = "提交评价")
    @PostMapping("/postReview")
    public Result postReview(@RequestHeader("token") String token, @RequestBody ReviewDTO reviewDTO) {
        return reviewService.postReview(token, reviewDTO);
    }

    @Operation(summary = "查看评价")
    @GetMapping("/getReview")
    public Result getReview(@RequestHeader("token") String token) {
        // 调用服务层方法获取评价信息
        return reviewService.getReviewInfo(token);
    }

}
