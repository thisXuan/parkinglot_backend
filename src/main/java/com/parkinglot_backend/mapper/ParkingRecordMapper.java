package com.parkinglot_backend.mapper;

import com.parkinglot_backend.entity.ParkingRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author minxuan
* @description 针对表【ParkingRecord】的数据库操作Mapper
* @createDate 2024-12-25 22:32:40
* @Entity com.parkinglot_backend.entity.ParkingRecord
*/
public interface ParkingRecordMapper extends BaseMapper<ParkingRecord> {
    @Select("SELECT parkingSpace FROM ParkingRecord WHERE id = #{id}")
    String selectParkingSpaceById(@Param("id") int id);
}




