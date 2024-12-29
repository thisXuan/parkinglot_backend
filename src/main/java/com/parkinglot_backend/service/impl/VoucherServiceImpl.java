package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parkinglot_backend.entity.Voucher;
import com.parkinglot_backend.service.VoucherService;
import com.parkinglot_backend.mapper.VoucherMapper;
import com.parkinglot_backend.util.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author minxuan
* @description 针对表【Voucher】的数据库操作Service实现
* @createDate 2024-12-29 16:08:26
*/
@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher>
    implements VoucherService{

    @Override
    public Result getVoucherByShopId(int shopId) {
        List<Voucher> vouchers = query().eq("storeId", shopId).list();
        if(vouchers==null){
            return Result.fail("未找到该优惠券");
        }
        return Result.ok(vouchers);
    }
}




