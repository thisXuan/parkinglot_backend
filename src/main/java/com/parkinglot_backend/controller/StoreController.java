package com.parkinglot_backend.controller;

import com.parkinglot_backend.service.StoreService;
import com.parkinglot_backend.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    /**
     * 获取店铺信息
     * @return
     */
    @GetMapping("/getStoreInfo")
    public Result getStoreInfo(@RequestParam int page){
        return storeService.getStoreInfo(page);
    }

    @GetMapping("/queryStoreInfo")
    public Result queryStoreInfo(@RequestParam String query){
        return storeService.queryStoreInfo(query);
    }

}
