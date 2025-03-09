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

    Result getServiceCategory(String query);

    Result getStoreName(String query);

    // 根据筛选条件获取商铺列表
    Result getStoresByFilters(String category, String floor, int page, int size);

    Result getStoreInfoById(int id);

    Result addFavoriteStore(String token, int storeId);

    Result removefavoriteStore(String token, int storeId);

    Result viewfavoritesStore(String token);

    Result viewLikesByStore(String token, int storeId);

    Result getAllName();
}
