package com.parkinglot_backend.service;

import com.parkinglot_backend.entity.Voucher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.parkinglot_backend.util.Result;

/**
* @author minxuan
* @description 针对表【Voucher】的数据库操作Service
* @createDate 2024-12-29 16:08:26
*/
public interface VoucherService extends IService<Voucher> {

    Result getVoucherByShopId(int shopId);
}
