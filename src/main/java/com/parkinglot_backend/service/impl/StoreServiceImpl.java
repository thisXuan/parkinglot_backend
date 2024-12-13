package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.parkinglot_backend.entity.Store;
import com.parkinglot_backend.service.StoreService;
import com.parkinglot_backend.mapper.StoreMapper;
import com.parkinglot_backend.util.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author minxuan
* @description 针对表【Store】的数据库操作Service实现
* @createDate 2024-10-27 11:16:24
*/
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store>
    implements StoreService{

    @Resource
    private StoreMapper storeMapper;

    @Override
    public Result getStoreInfo(int page) {
        // 设置分页, 固定设置每页查询为10页
        PageHelper.startPage(page,10);
        List<Store> list = query().list();
        PageInfo<Store> pageInfo = new PageInfo<>(list);
        return Result.ok(pageInfo.getList());
    }

    @Override
    public Result queryStoreInfo(String query) {
        List<Store> stores = storeMapper.searchStore(query);
        if(stores.isEmpty()){
            return Result.fail("未查找到相关店铺");
        }
        return Result.ok(stores);
    }

    @Override
    public Result getServiceCategory(String query) {
        List<Store> stores;
        if ("全部".equals(query)) {
            stores = storeMapper.findAllStores(); // 这个方法返回所有商店
        } else {
            stores = storeMapper.findStoresByCategory(query); // 根据类别查询商店
        }
        if (stores.isEmpty()) {
            return Result.fail("未查找到相关店铺");
        }
        return Result.ok(stores);
    }

    @Override
    public Result getStoreName() {
        List<String> allStoreNames = storeMapper.findAllStoreNames();
        return Result.ok(allStoreNames);
    }


}




