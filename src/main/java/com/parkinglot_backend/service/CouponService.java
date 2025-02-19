package com.parkinglot_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parkinglot_backend.entity.Coupon;
import com.parkinglot_backend.util.Result;

public interface CouponService{
    Result getCoupon(String type) ;

    Result exchangeCoupon(String token, Integer id);
}
