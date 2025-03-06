package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parkinglot_backend.dto.Coordinates;
import com.parkinglot_backend.dto.ParkingTimeAndLocationDTO;
import com.parkinglot_backend.entity.Car;
import com.parkinglot_backend.entity.ParkingPoint;
import com.parkinglot_backend.entity.ParkingRecord;
import com.parkinglot_backend.mapper.CarMapper;
import com.parkinglot_backend.mapper.ParkingPointMapper;
import com.parkinglot_backend.mapper.ParkingRecordMapper;
import com.parkinglot_backend.mapper.ParkingSpotMapper;
import com.parkinglot_backend.service.CarService;
import com.parkinglot_backend.service.ParkingService;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ParkingServiceImpl extends ServiceImpl<ParkingRecordMapper,ParkingRecord> implements ParkingService {

    @Resource
    private ParkingRecordMapper parkingRecordMapper;
    @Resource
    private ParkingPointMapper parkingPointMapper;

    @Resource
    private ParkingSpotMapper parkingSpotMapper;

    @Resource
    private CarMapper carMapper;

    @Resource
    private CarService carService;

    @Override
    public Result getParkingTimeAndLocation(String carName) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("carName", carName);
        Car car = carMapper.selectOne(queryWrapper);
        if(car ==null){
            return Result.fail("车辆不存在！");
        }
        String carname = car.getCarname();
        QueryWrapper<ParkingRecord> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("carName", carname).isNull("exitTime");
        ParkingRecord parkingRecord = parkingRecordMapper.selectOne(queryWrapper1);
        if(parkingRecord==null){
            return Result.fail("未查询到停车记录！");
        }
        Date entrytime = parkingRecord.getEntrytime();
        String parkingspace = parkingRecord.getParkingSpace();
        ParkingTimeAndLocationDTO parkingTimeAndLocationDTO = new ParkingTimeAndLocationDTO();
        parkingTimeAndLocationDTO.setParkingTime(entrytime);
        parkingTimeAndLocationDTO.setLocation(parkingspace);
        return Result.ok(parkingTimeAndLocationDTO);
    }

    @Override
    public Result entryParking(String token, ParkingRecord parkingRecord) {
        // 传入的parkingRecord只有carName和parkingSpace
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        // 判断是否有未支付的情况
        QueryWrapper<ParkingRecord> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("carName", parkingRecord.getCarname()).isNull("exitTime");
        ParkingRecord parkingRecord1 = parkingRecordMapper.selectOne(queryWrapper1);
        if(parkingRecord1!=null){
            return Result.fail("车辆已在停车场中！");
        }
        // 根据用户id和车牌信息获取车辆信息
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId).eq("carName", parkingRecord.getCarname());
        Car car = carMapper.selectOne(queryWrapper);
        // 获取不到。新建存储
        if(car ==null){
            car = new Car();
            car.setCarname(parkingRecord.getCarname());
            car.setUpdatetime(new Date());
            car.setUserid(userId);
            carService.save(car);
        }
        parkingRecord.setEntrytime(new Date());
        log.info("停车时间为"+parkingRecord.getEntrytime());
        boolean save = save(parkingRecord);
        if(!save){
            return Result.fail("更新失败");
        }
        return Result.ok("更新成功");
    }

    @Override
    public Result leaveParking(String token, ParkingRecord parkingRecord) {
        String carname = parkingRecord.getCarname();
        QueryWrapper<ParkingRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("carName", carname).isNull("exitTime");
        ParkingRecord parkingRecord1 = parkingRecordMapper.selectOne(queryWrapper);
        if(parkingRecord1==null){
            return Result.fail("未查找到该车辆的停车信息！");
        }
        Date entrytime = new Date();
        parkingRecord1.setPayment(calculateParkingFee(parkingRecord1.getEntrytime(), entrytime));
        parkingRecord1.setExittime(entrytime);
        updateById(parkingRecord1);
        return Result.ok("操作成功");
    }

    @Override
    public Result getMyLocation(String token) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        //System.out.println("userId"+userId);
        String parking_space = parkingRecordMapper.selectParkingSpaceById(userId);
        //System.out.println("parking_space"+parking_space);
        Integer spotId = parkingSpotMapper.findSpotIdBySpotName(parking_space);
        //System.out.println("spotId"+spotId);
        Coordinates coordinates = parkingPointMapper.selectXYCoordinatesById(spotId);
        //System.out.println(coordinates);
        return Result.ok(coordinates);
    }

    @Override
    public Result getNoCarLocation(String token) {
        List<Integer>  listSpotId = parkingSpotMapper.selectSpotIdsByOccupiedStatus(false);
        //System.out.println(listSpotId);
        List<Coordinates> coordinatesList = parkingPointMapper.selectCoordinatesByIds(listSpotId);
        //System.out.println(coordinatesList);
        return Result.ok(coordinatesList);
    }

    @Override
    public Result getPayment(String token) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);

        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Car> cars = carMapper.selectList(queryWrapper);

        List<ParkingRecord> parkingRecords = new ArrayList<>();
        for(Car car : cars){
            QueryWrapper<ParkingRecord> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("carName", car.getCarname());
            List<ParkingRecord> parkingRecords1 = parkingRecordMapper.selectList(queryWrapper1);
            parkingRecords.addAll(parkingRecords1);
        }
        return Result.ok(parkingRecords);
    }

    public String calculateParkingFee(Date entryTime, Date exitTime) {
        if(exitTime==null){
            exitTime = new Date();
        }
        long durationMillis = exitTime.getTime() - entryTime.getTime();
        long minutesStayed = TimeUnit.MILLISECONDS.toMinutes(durationMillis);
        if (minutesStayed <= 15) {
            return String.valueOf(0);
        }
        // 停车总费用初始化
        int totalFee = 0;
        // 将分钟转换为天和剩余分钟
        long daysStayed = minutesStayed / (24 * 60);
        long remainingMinutes = minutesStayed % (24 * 60);
        // 每天封顶费用为10元
        totalFee += daysStayed * 10;
        if (remainingMinutes > 0) {
            // 剩余时间按小时收费
            long hoursStayed = (remainingMinutes + 59) / 60; // 不足1小时按1小时计算
            totalFee += Math.min(hoursStayed * 2, 10); // 封顶10元
        }
        return String.valueOf(totalFee);
    }

    @Override
    public Result getMyCar(String token) {
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);

        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Car> cars = carMapper.selectList(queryWrapper);
        return Result.ok(cars);
    }


}
