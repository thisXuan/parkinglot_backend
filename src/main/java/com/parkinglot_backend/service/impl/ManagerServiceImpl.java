package com.parkinglot_backend.service.impl;

import com.parkinglot_backend.dto.ChangeNameDTO;
import com.parkinglot_backend.mapper.ShopLocationMapper;
import com.parkinglot_backend.mapper.StoreMapper;
import com.parkinglot_backend.mapper.StorePointMapper;
import com.parkinglot_backend.service.ManagerService;
import com.parkinglot_backend.util.Result;
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

    @Override
    public Result changeStoreLocation(ChangeNameDTO changeNameDTO) {
        String beforeName = changeNameDTO.getBeforeName();
        String afterName = changeNameDTO.getAfterName();
        int res = shopLocationMapper.updateShopLocationName(beforeName,afterName);
        if(res == 0){
            return Result.fail("没有这个店铺");
        }
        Integer storeId = storeMapper.getIdByStoreName(beforeName);
        if(storeId == null){
            return Result.fail("没有这个店铺1");
        }
        int res_update = storeMapper.updateStoreNameAndCategory(storeId,afterName);
        if(res_update == 0){
            return Result.fail("更新失败");
        }
        return Result.ok("更新成功，后续联系管理员修改店铺图片");
    }
}
