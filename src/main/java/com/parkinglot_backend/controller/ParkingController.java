package com.parkinglot_backend.controller;

import com.parkinglot_backend.entity.ParkingRecord;
import com.parkinglot_backend.service.ParkingService;
import com.parkinglot_backend.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking")
@Tag(name = "反向寻车部分")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    /**
     * 获取用户停车时间和停车位置
     * @param carName
     * @return
     */
    @Operation(summary = "获取用户停车时间和停车位置")
    @GetMapping("/getParkingTimeAndLocation")
    public Result getParkingTimeAndLocation(@RequestHeader("token")String token,@RequestParam("carName")String carName) {
        return parkingService.getParkingTimeAndLocation(carName);
    }

    /**
     * 停车场入场接口
     * @param parkingRecord
     * @return
     */
    @Operation(summary = "停车场入场接口")
    @PostMapping("/entryParking")
    public Result entryParking(@RequestHeader("token")String token,@RequestBody ParkingRecord parkingRecord) {
        return parkingService.entryParking(token,parkingRecord);
    }

    /**
     * 停车场离场接口
     * @param token
     * @param parkingRecord
     * @return
     */
    @Operation(summary = "停车场离场接口")
    @PostMapping("/leaveParking")
    public Result leaveParking(@RequestHeader("token")String token,@RequestBody ParkingRecord parkingRecord) {
        return parkingService.leaveParking(token,parkingRecord);
    }

    /**
     * 获取缴费记录
     * @return
     */
    @Operation(summary = "获取缴费记录")
    @GetMapping("/getPayment")
    public Result getPayment(@RequestHeader("token")String token){
        return parkingService.getPayment(token);
    }

    /**
     * 获取我的车辆信息
     * @param token
     * @return
     */
    @Operation(summary = "获取我的车辆信息")
    @GetMapping("/getMyCar")
    public Result getMyCar(@RequestHeader("token")String token){
        return parkingService.getMyCar(token);
    }

    @Operation(summary = "获取我的车辆位置")
    @GetMapping("/location")
    public Result getLocation(@RequestHeader("token")String token){
        return parkingService.getMyLocation(token);
    }

    @Operation(summary = "获取空车位")
    @GetMapping("/nulllocation")
    public Result getNullLocation(@RequestHeader("token")String token){
        return parkingService.getNoCarLocation(token);
    }

    @Operation(summary = "获取被占据车位")
    @GetMapping("/fulllocation")
    public Result getFullLocation(@RequestHeader("token")String token){
        return parkingService.getHasCarLocation(token);
    }
}
