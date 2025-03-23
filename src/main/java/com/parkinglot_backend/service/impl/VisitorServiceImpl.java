package com.parkinglot_backend.service.impl;

import com.parkinglot_backend.dto.DataDTO;
import com.parkinglot_backend.mapper.ParkingSpotMapper;
import com.parkinglot_backend.mapper.VisitorMapper;
import com.parkinglot_backend.service.VisitorService;
import com.parkinglot_backend.util.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-23
 * @Description:
 */

@Service
public class VisitorServiceImpl implements VisitorService {

    @Resource
    private ParkingSpotMapper parkingSpotMapper;

    @Resource
    private VisitorMapper visitorMapper;
    @Override
    public Result getTotalViewService(String token) {
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
}
