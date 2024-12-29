package com.parkinglot_backend.controller;

import com.parkinglot_backend.service.VoucherService;
import com.parkinglot_backend.util.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/voucher")
@Tag(name = "优惠券部分")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/getVoucherByShopId")
    public Result getVoucherByShopId(@RequestParam("shopId") int shopId) {
        return voucherService.getVoucherByShopId(shopId);
    }
}
