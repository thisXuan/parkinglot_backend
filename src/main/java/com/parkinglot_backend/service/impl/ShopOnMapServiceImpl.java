package com.parkinglot_backend.service.impl;

import com.parkinglot_backend.dto.StoreDTO;
import com.parkinglot_backend.mapper.ShopLocationMapper;
import com.parkinglot_backend.mapper.StorePointMapper;
import com.parkinglot_backend.service.ShopOnMapService;
import com.parkinglot_backend.util.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-01-20
 * @Description:
 */

@Service
public class ShopOnMapServiceImpl implements ShopOnMapService {

    @Resource
    private StorePointMapper storePointMapper;

    @Resource
    private ShopLocationMapper shopLocationMapper;

    // 定义一个全局变量缓存商铺信息
    private List<StoreDTO> cachedStoreDetails;

    @Override
    public Result getAllShopLocations() {
        // 如果缓存为空，从数据库中加载数据
        if (cachedStoreDetails == null || cachedStoreDetails.isEmpty()) {
            cachedStoreDetails = shopLocationMapper.selectAllShopLocations();
        }

        // 判断是否获取到数据并封装返回结果
        if (cachedStoreDetails != null && !cachedStoreDetails.isEmpty()) {
            // 打印缓存的商铺信息
            System.out.println("Cached Store Details: " + cachedStoreDetails);
            return Result.ok(cachedStoreDetails); // 封装成功结果
        } else {
            return Result.fail("没有找到商铺"); // 如果没有找到数据，返回错误信息
        }
    }

    @Override
    public Result getShopLocationsByFloor(String floor) {
        // 确保缓存已加载
        if (cachedStoreDetails == null || cachedStoreDetails.isEmpty()) {
            cachedStoreDetails = shopLocationMapper.selectAllShopLocations();
        }
        // 打印缓存的商铺信息
        System.out.println("Cached Store Details: " + cachedStoreDetails);
        // 筛选符合楼层条件的商铺
        List<StoreDTO> filteredStoreDetails = cachedStoreDetails.stream()
                .filter(store -> floor.equals(store.getFloorNumber()))
                .toList();

        // 判断筛选结果并封装返回
        if (!filteredStoreDetails.isEmpty()) {
            return Result.ok(filteredStoreDetails);
        } else {
            return Result.fail("指定楼层没有找到商铺");
        }
    }
}
