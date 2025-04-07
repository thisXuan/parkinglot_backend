package com.parkinglot_backend.service.impl;

import com.parkinglot_backend.dto.*;
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
import java.util.*;

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

        LocalDate previousDate = date.minusDays(1);

        // 从 MySQL 中获取访问人数
        Integer visitorCount = visitorMapper.getVisitorCountByDate(dateString);
        if (visitorCount == null) {
            // 如果 MySQL 中没有当天的数据，则插入一条新的记录，访问人数为0
            visitorMapper.insertVisitorCount(dateString, 0);
            visitorCount = 0;
        }
        //Random random = new Random();
        Integer dayOrderCount = orderMapper.getTodayOrderCount();
        Integer yesterdayOrder = orderMapper.getYesterdayOrderCount();
        Integer dayOrderDay = 100*(dayOrderCount-yesterdayOrder)/dayOrderCount;

        Integer weekOrder = orderMapper.getRecentSevenDaysOrderCount();
        Integer lastFourteenOrder = orderMapper.getRecentFourteenDaysOrderCount();
        Integer lastWeekOrder = lastFourteenOrder-weekOrder;
        Integer weekOrderWeek = 100*(weekOrder-lastWeekOrder)/weekOrder;

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
        Map<LocalDate, Integer> salesMap = new HashMap<>();

        // 填充过去七天的日期
        LocalDate date = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            salesMap.put(date.minusDays(i), 0);
        }

        // 更新实际销售额数据
        for (SalesDataDTO data : salesData) {
            salesMap.put(data.getSaleDate(), data.getTotalSales());
        }

        // 构建返回列表
        for (LocalDate d : salesMap.keySet()) {
            salesDataList.add(salesMap.get(d));
        }
        Collections.reverse(salesDataList);
        return Result.ok(salesDataList);
    }

    @Override
    public Result getUserAnalysisService(String token) {
        boolean tokenVa = tokenVaild(token);
        if(!tokenVa){
            return Result.fail("非管理员无查看资质");
        }
        int totalRegister = userMapper.getTotalUserCount();
        //System.out.println(totalRegister);
        int todayRegister = userMapper.getNewUsersToday();
        //System.out.println(todayRegister);
        List<RegistrationCount> weekRegister = userMapper.getNewUsersInLastSevenDays();
        System.out.println(weekRegister);
        List<Integer> weekRegisterList = new ArrayList<>();
        Map<LocalDate, Integer> weekRegisterMap = new HashMap<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            weekRegisterMap.put(now.minusDays(i), 0);
        }
        for (RegistrationCount data : weekRegister) {
            weekRegisterMap.put(data.getRegDate(), data.getNewUserCount());
        }
        // 构建返回列表
        for (LocalDate d : weekRegisterMap.keySet()) {
            weekRegisterList.add(weekRegisterMap.get(d));
        }
        Collections.reverse(weekRegisterList);

        UserAnalyticsDTO dataDTO = new UserAnalyticsDTO();
        dataDTO.setTotalRegister(totalRegister);
        dataDTO.setTodayRegister(todayRegister);
        dataDTO.setWeekRegister(weekRegisterList);

        return Result.ok(dataDTO);

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
