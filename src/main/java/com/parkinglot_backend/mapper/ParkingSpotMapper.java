package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.Store;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.ParkingSpot;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
public interface ParkingSpotMapper extends BaseMapper<ParkingSpot> {
    @Select("SELECT spot_id FROM ParkingSpots WHERE spot_id=#{storeId}")
    Integer findPointIdByParkId(Integer storeId);


}
