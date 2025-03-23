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

    @Select("SELECT spot_name FROM ParkingSpots")
    List<String> findAllName();

    @Select("SELECT spot_id FROM ParkingSpots WHERE spot_name=#{spotName}")
    Integer findSpotIdBySpotName(@Param("spotName") String spotName);

    // 返回所有 is_occupied 等于某个值的 spot_id 列表
    @Select("SELECT spot_id FROM ParkingSpots WHERE is_occupied = #{isOccupied}")
    List<Integer> selectSpotIdsByOccupiedStatus(@Param("isOccupied") boolean isOccupied);

    @Select("SELECT spot_name FROM ParkingSpots WHERE spot_name LIKE CONCAT('%', #{query}, '%')")
    List<String> findSpotNamesByQuery(@Param("query") String query);

    // 查询 is_occupied 列中值为0的个数
    @Select("SELECT COUNT(*) FROM ParkingSpots WHERE is_occupied = 0")
    Integer countUnoccupiedSpots();
}
