package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.parkinglot_backend.entity.Store;
import com.parkinglot_backend.mapper.ParkingSpotMapper;
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

    @Resource
    private ParkingSpotMapper parkingSpotMapper;

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
        List<String> allNames = storeMapper.findAllStoreNames();
        List<String> parkingNames = parkingSpotMapper.findAllName();
        allNames.addAll(parkingNames);
        return Result.ok(allNames);
    }

    @Override
    public Result getStoresByFilters(String category, String floor, int page, int size) {
        // 对 floor 参数进行转换
        if ("B1".equals(floor)) {
            floor = "-1";
        } else if ("M".equals(floor)) {
            floor = "0";
        }

        // 自动处理分页，size固定为10
        PageHelper.startPage(page, 10);
        //System.out.println(floor);

        // 调用 StoreMapper 获取筛选后的商铺列表
        List<Store> stores = storeMapper.getStoresByFilters(category, floor);

        // 返回结果
        return Result.ok(stores);
    }

    @Override
    public Result getStoreInfoById(int id) {
        Store store = storeMapper.selectById(id);
        if(store == null){
            return Result.fail("该店铺不存在！");
        }
        return Result.ok(store);
    }


}




