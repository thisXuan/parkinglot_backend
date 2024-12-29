package com.parkinglot_backend.service.impl;

import com.parkinglot_backend.dto.QRCodeRequest;
import com.parkinglot_backend.mapper.QRCodeScansMapper;
import com.parkinglot_backend.service.QRcodeService;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-12-24
 * @Description: 记录扫描得到的二维码
 */


@Service
public class QRcodeServiceImpl implements QRcodeService {

    @Resource
    private QRCodeScansMapper qrCodeScansMapper;

    @Override
    public Result<?> submitQRCode(QRCodeRequest qrCodeRequest, String token) {
        try {
            // 验证 token
            Claims claims = JwtUtils.parseJWT(token.replace("Bearer ", ""));
            Integer userId = claims.get("UserId", Integer.class);

            // 处理二维码内容
            String qrCodeContent = qrCodeRequest.getQrCodeContent();
            System.out.println("UserId: " + userId);
            System.out.println("QRCodeContent: " + qrCodeContent);

            // 检查是否存在记录
            int count = qrCodeScansMapper.countByUserId(userId);

            if (count > 0) {
                // 更新记录
                int rowsAffected = qrCodeScansMapper.updateQRCodeContentByUserId(userId, qrCodeContent);
                if (rowsAffected > 0) {
                    return Result.ok("QRCode updated successfully");
                } else {
                    return Result.fail("Failed to update QRCode");
                }
            } else {
                // 插入记录
                int rowsInserted = qrCodeScansMapper.insertQRCodeScan(userId, qrCodeContent);
                if (rowsInserted > 0) {
                    return Result.ok("QRCode inserted successfully");
                } else {
                    return Result.fail("Failed to insert QRCode");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Invalid token");
        }
    }

    public Result getQRCodeContentByUserId(String token) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);

        if (userId == null) {
            return Result.fail("用户ID不能为空");
        }

        try {
            String qrContent = qrCodeScansMapper.selectQRCodeByUserId(userId);
            if (qrContent == null) {
                return Result.fail("未找到对应的二维码内容，用户ID: " + userId);
            }
            System.out.println(qrContent);
            return Result.ok(qrContent);
        } catch (Exception e) {
            return Result.fail("获取二维码内容失败: " + e.getMessage());
        }
    }

}
