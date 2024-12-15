package com.parkinglot_backend.controller;

import com.parkinglot_backend.service.StoreService;
import com.parkinglot_backend.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/store")
@Tag(name = "商场部分")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Operation(summary = "获取店铺信息")
    @GetMapping("/getStoreInfo")
    public Result getStoreInfo(@RequestParam int page){
        return storeService.getStoreInfo(page);
    }

    @Operation(summary = "输入店铺名字一部分查找商铺")
    @GetMapping("/queryStoreInfo")
    public Result queryStoreInfo(@RequestParam String query){
        return storeService.queryStoreInfo(query);
    }

    @Operation(summary = "根据种类返回商铺")
    @GetMapping("/getServiceCategory")
    public Result getServiceCategory(@RequestParam String query){
        return storeService.getServiceCategory(query);
    }

    @Operation(summary = "返回所有商铺名字")
    @GetMapping("/getStoreName")
    public Result getStoreName(){
        return storeService.getStoreName();
    }

}
