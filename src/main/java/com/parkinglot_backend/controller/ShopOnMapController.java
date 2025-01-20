package com.parkinglot_backend.controller;

import com.parkinglot_backend.dto.StoreDTO;
import com.parkinglot_backend.service.ShopOnMapService;
import com.parkinglot_backend.util.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-01-20
 * @Description:
 */

@Slf4j
@RestController
@RequestMapping("/shopLocation")
@Tag(name = "商铺在地图上展示")
public class ShopOnMapController {

    @Autowired
    private ShopOnMapService shopOnMapService;
    @GetMapping("/all")
    public Result getAllShopsOnMap() {
        // 假设从数据库中获取所有商铺位置信息
        return shopOnMapService.getAllShopLocations();
    }

    @GetMapping("/byFloor")
    public Result getShopsByFloor(@RequestParam("floor") String floor) {
        // 调用服务层方法获取指定楼层的商铺位置信息
        return shopOnMapService.getShopLocationsByFloor(floor);
    }

}
