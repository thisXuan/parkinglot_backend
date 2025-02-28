package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parkinglot_backend.entity.Order;
import com.parkinglot_backend.entity.Voucher;
import com.parkinglot_backend.mapper.OrderMapper;
import com.parkinglot_backend.service.VoucherService;
import com.parkinglot_backend.mapper.VoucherMapper;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.RedisIdWorker;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author minxuan
* @description 针对表【Voucher】的数据库操作Service实现
* @createDate 2024-12-29 16:08:26
*/
@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher>
    implements VoucherService{

    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private VoucherMapper voucherMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public Result getVoucherByShopId(int shopId) {
        List<Voucher> vouchers = query().eq("storeId", shopId).list();
        if(vouchers==null){
            return Result.fail("未找到该优惠券");
        }
        return Result.ok(vouchers);
    }

    @Override
    public Result buyVoucher(String token, int voucherId) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        int stock = voucherMapper.selectStockById(voucherId);
        if(stock <= 0){
            return Result.fail("没有库存，购买失败");
        }
        double pay_Value = voucherMapper.selectPayValueById(voucherId);
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        orderMapper.insertOrder(redisIdWorker.nextId("order"), userId, voucherId, now, pay_Value, 2);

        // 更新优惠券库存
        voucherMapper.updateStock(voucherId);

        return Result.ok("购买成功");
    }

    @Override
    public Result getOrder(String token, Integer type) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        if (type == null) {
            // type为空，返回该用户所有订单
            List<Order> orders = orderMapper.selectOrdersByUserId(userId);
            return Result.ok(orders);
        } else {
            // type不为空，返回该type订单
            List<Order> ordersByType = orderMapper.selectOrdersByUserIdAndType(userId, type);
            return Result.ok(ordersByType);
        }

    }
}




