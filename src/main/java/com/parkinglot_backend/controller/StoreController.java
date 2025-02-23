package com.parkinglot_backend.controller;

import com.parkinglot_backend.service.StoreService;
import com.parkinglot_backend.service.VoucherService;
import com.parkinglot_backend.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/store")
@Tag(name = "商场部分")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private VoucherService voucherService;

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

    @Operation(summary = "根据店铺id返回店铺数据")
    @GetMapping("/getStoreInfoById")
    public Result getStoreInfoById(@RequestParam int id){
        return storeService.getStoreInfoById(id);
    }

    // 获取符合条件的商铺列表
    @GetMapping("/get_stores")
    public Result getStores(@RequestParam String category,
                            @RequestParam String floor,
                            @RequestParam int page,
                            @RequestParam int size) {
        //System.out.println(floor);
        // 调用服务层获取商铺数据
        return storeService.getStoresByFilters(category, floor, page, size);
    }

    @Operation(summary = "收藏商铺")
    @PostMapping("/likes")
    public Result addFavorite(@RequestHeader("token")String token,@RequestParam int store_id){
        return storeService.addFavoriteStore(token,store_id);
    }

    @Operation(summary = "取消收藏商铺")
    @PostMapping("/removeLikes")
    public Result removeFavorite(@RequestHeader("token")String token,@RequestParam int store_id){
        return storeService.removefavoriteStore(token,store_id);
    }

    @Operation(summary = "取消收藏商铺")
    @GetMapping("/viewLikes")
    public Result viewFavorite(@RequestHeader("token")String token){
        return storeService.viewfavoritesStore(token);
    }

    @Operation(summary = "购买优惠券")
    @PostMapping("/buyVoucher")
    public Result buyVoucherInStore(@RequestHeader("token")String token , @RequestParam int voucher_id){
        return voucherService.buyVoucher(token,voucher_id);
    }

    @Operation(summary = "查看我的订单")
    @GetMapping("/getOrder")
    public Result getMyOrder(@RequestHeader("token")String token , @RequestParam(value = "type", required = false) Integer type){
        return voucherService.getOrder(token,type);
    }

}
