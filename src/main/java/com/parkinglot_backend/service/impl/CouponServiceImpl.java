package com.parkinglot_backend.service.impl;

import com.parkinglot_backend.entity.Coupon;
import com.parkinglot_backend.mapper.CouponMapper;
import com.parkinglot_backend.mapper.UserMapper;
import com.parkinglot_backend.service.CouponService;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-02-19
 * @Description:
 */

@Service
public class CouponServiceImpl implements CouponService {

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Result getCoupon(String type) {
        List<Coupon> coupons = null;
        if (type == null || type.isEmpty()) {
            // 如果type为空或空字符串，调用findByStoreId方法
            // 这里假设调用findByStoreId时需要一个默认的storeId，例如1
            coupons = couponMapper.findAllCoupons();
        } else {
            // 如果type不为空，调用findByServiceType方法
            coupons = couponMapper.findByServiceCategory(type);
        }
        return Result.ok(coupons);
    }

    @Override
    public Result exchangeCoupon(String token, Integer id) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        Integer currentPoint = userMapper.getUserPointByUserId(userId); // 获取当前用户的成长值
        Integer payPoint = couponMapper.findPayPointById(id);

        if(payPoint>currentPoint){
            return Result.fail("成长值不足");
        }
        userMapper.updatePointByUserId(userId,currentPoint-payPoint);
        return Result.ok(true);
    }
}
