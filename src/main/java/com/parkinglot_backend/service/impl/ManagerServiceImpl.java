package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.parkinglot_backend.dto.ChangeNameDTO;
import com.parkinglot_backend.entity.Store;
import com.parkinglot_backend.mapper.ShopLocationMapper;
import com.parkinglot_backend.mapper.StoreMapper;
import com.parkinglot_backend.mapper.StorePointMapper;
import com.parkinglot_backend.mapper.UserMapper;
import com.parkinglot_backend.service.ManagerService;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-05
 * @Description:
 */

@Service
public class ManagerServiceImpl implements ManagerService {
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private StorePointMapper storePointMapper;
    @Resource
    private ShopLocationMapper shopLocationMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result changeStoreLocation(String token, Store store) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        int type = userMapper.getUserTypeById(userId);
        if(type == 0){
            return Result.fail("非管理员无修改资质");
        }

        // 获取store的id
        Integer storeId = store.getId();
        if (storeId == null) {
            return Result.fail("Store id 不存在");
        }
        String beforeName = storeMapper.getStoreNameById(storeId);
        String afterName = store.getStoreName();
        int res = shopLocationMapper.updateShopLocationName(beforeName,afterName);
        if(res == 0){
            return Result.fail("没有这个店铺");
        }
        // 构造更新条件
        UpdateWrapper<Store> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", storeId);

        // 将store对象的id设置为null，避免更新id字段
        store.setId(null);

        // 更新数据库中对应行的其他内容
        int updateRows = storeMapper.update(store, updateWrapper);
        if (updateRows > 0) {
            return Result.ok("商铺更新成功");
        } else {
            return Result.fail("商铺更新失败");
        }
    }

//    @Override
//    public Result changeStoreLocation(String token, Store store) {
//        Claims claims = JwtUtils.parseJWT(token);
//        Integer userId = claims.get("UserId", Integer.class);
//        int type = userMapper.getUserTypeById(userId);
//        if(type == 0){
//            return Result.fail("非管理员无修改资质");
//        }
//
//        // 获取store的id
//        Integer storeId = store.getId();
//        if (storeId == null) {
//            return Result.fail("Store id 不存在");
//        }
//
//        String beforeName = changeNameDTO.getBeforeName();
//        String afterName = changeNameDTO.getAfterName();
//        int res = shopLocationMapper.updateShopLocationName(beforeName,afterName);
//        if(res == 0){
//            return Result.fail("没有这个店铺");
//        }
//        Integer storeId = storeMapper.getIdByStoreName(beforeName);
//        if(storeId == null){
//            return Result.fail("没有这个店铺1");
//        }
//        int res_update = storeMapper.updateStoreNameAndCategory(storeId,afterName);
//        if(res_update == 0){
//            return Result.fail("更新失败");
//        }
//        return Result.ok("更新成功，后续联系管理员修改店铺图片");
//    }


}
