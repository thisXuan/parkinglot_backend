package com.parkinglot_backend.controller;

import com.parkinglot_backend.dto.QRCodeRequest;
import com.parkinglot_backend.service.QRcodeService;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.parkinglot_backend.dto.ApiResponse;
import com.parkinglot_backend.entity.User;
/**
 * @Author: HeYuxin
 * @CreateTime: 2024-12-24
 * @Description: 二维码请求处理
 */


@RestController
@RequestMapping("/qrcode")
public class QRCodeController {

    @Autowired
    private QRcodeService qrCodeService;

    //这里前端使用POST但是后端使用GET,原因不知
    @PostMapping("/submit")
    public Result<?> submitQRCode(@RequestBody QRCodeRequest qrCodeRequest, @RequestHeader("token") String token, HttpServletRequest httpServletRequest) {
        System.out.println("接收到的请求方法: POST");
        System.out.println("接收到的二维码内容: " + qrCodeRequest.getQrCodeContent());
        System.out.println("接收到的 Authorization: " + token);
        return qrCodeService.submitQRCode(qrCodeRequest, token);
    }

//    @GetMapping("/submit")
//    public Result<?> handleGet() {
//        System.out.println("接收到的请求方法: GET");
//        return Result.fail("GET method is not supported for this endpoint.");
//    }
}
