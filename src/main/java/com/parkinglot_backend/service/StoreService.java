package com.parkinglot_backend.service;

import com.parkinglot_backend.entity.Store;
import com.baomidou.mybatisplus.extension.service.IService;
import com.parkinglot_backend.util.Result;

/**
* @author minxuan
* @description 针对表【Store】的数据库操作Service
* @createDate 2024-10-27 11:16:24
*/
public interface StoreService extends IService<Store> {

    Result getStoreInfo(int page);

    Result queryStoreInfo(String query);
}
