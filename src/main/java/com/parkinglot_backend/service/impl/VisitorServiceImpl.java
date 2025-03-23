package com.parkinglot_backend.service.impl;

import com.parkinglot_backend.dto.DataDTO;
import com.parkinglot_backend.dto.OrderDTO;
import com.parkinglot_backend.dto.SalesDataDTO;
import com.parkinglot_backend.mapper.OrderMapper;
import com.parkinglot_backend.mapper.ParkingSpotMapper;
import com.parkinglot_backend.mapper.UserMapper;
import com.parkinglot_backend.mapper.VisitorMapper;
import com.parkinglot_backend.service.VisitorService;
import com.parkinglot_backend.util.JwtUtils;
import com.parkinglot_backend.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-23
 * @Description:
 */

@Service
public class VisitorServiceImpl implements VisitorService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private ParkingSpotMapper parkingSpotMapper;

    @Resource
    private VisitorMapper visitorMapper;

    @Resource
    private OrderMapper orderMapper;
    @Override
    public Result getTotalViewService(String token) {
        boolean tokenVa = tokenVaild(token);
        if(!tokenVa){
            return Result.fail("非管理员无查看资质");
        }
        // 获取当前日期
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = date.format(formatter);

        // 从 MySQL 中获取访问人数
        Integer visitorCount = visitorMapper.getVisitorCountByDate(dateString);
        if (visitorCount == null) {
            // 如果 MySQL 中没有当天的数据，则插入一条新的记录，访问人数为0
            visitorMapper.insertVisitorCount(dateString, 0);
            visitorCount = 0;
        }
        int storeCount = 20; // 假设值
        int parkingCount = parkingSpotMapper.countUnoccupiedSpots();

        DataDTO dateDTO = new DataDTO();
        dateDTO.setVisitor(visitorCount);
        dateDTO.setParking(parkingCount);
        dateDTO.setStore(storeCount);
        return Result.ok(dateDTO);
    }

    @Override
    public Result getOrderAnalysisService(String token) {
        boolean tokenVa = tokenVaild(token);
        if(!tokenVa){
            return Result.fail("非管理员无查看资质");
        }
        // 获取当前日期
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = date.format(formatter);

        // 从 MySQL 中获取访问人数
        Integer visitorCount = visitorMapper.getVisitorCountByDate(dateString);
        if (visitorCount == null) {
            // 如果 MySQL 中没有当天的数据，则插入一条新的记录，访问人数为0
            visitorMapper.insertVisitorCount(dateString, 0);
            visitorCount = 0;
        }
        Random random = new Random();
        Integer dayOrderDay = random.nextInt(99)+1; //随机数1-99
        Integer weekOrderWeek = random.nextInt(99)+1; //随机数1-99

        List<SalesDataDTO> salesData = orderMapper.getSalesDataForLastSevenDays();
        Integer weekOrder = salesData.size();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTodayOrder(visitorCount);
        orderDTO.setWeekOrder(weekOrder);
        orderDTO.setWeekOrderWeek(weekOrderWeek);
        orderDTO.setDayOrderDay(dayOrderDay);
        return  Result.ok(orderDTO);
    }

    @Override
    public Result getSalesAnalysisService(String token) {
        boolean tokenVa = tokenVaild(token);
        if(!tokenVa){
            return Result.fail("非管理员无查看资质");
        }
        List<SalesDataDTO> salesData = orderMapper.getSalesDataForLastSevenDays();
        List<Integer> salesDataList = new ArrayList<>();
        for (SalesDataDTO data : salesData) {
            salesDataList.add(data.getTotalSales());
        }

        return Result.ok(salesData);
    }

    private boolean tokenVaild(String token){
        Claims claims = JwtUtils.parseJWT(token);
        Integer userId = claims.get("UserId", Integer.class);
        int type = userMapper.getUserTypeById(userId);
        if(type == 0){
            return false;
        }
        return  true;
    }
}
