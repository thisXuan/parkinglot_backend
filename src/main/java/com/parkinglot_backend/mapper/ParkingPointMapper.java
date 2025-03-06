package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.dto.Coordinates;
import com.parkinglot_backend.entity.ParkingPoint;
import com.parkinglot_backend.entity.Points;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ParkingPointMapper extends BaseMapper<ParkingPoint> {

    @Select("SELECT id, x_coordinate, y_coordinate, floor, is_elevator FROM parking_lot")
    List<ParkingPoint> selectAllCoordinates();

    @Select("SELECT id, x_coordinate, y_coordinate, floor, is_elevator FROM parking_lot WHERE id = #{id}")
    Points selectCoordinatesById(int id);

    @Select("SELECT x_coordinate, y_coordinate FROM parking_lot WHERE id = #{id}")
    Coordinates selectXYCoordinatesById(@Param("id") Integer id);

    @Select("<script>" +
            "SELECT x_coordinate, y_coordinate FROM parking_lot WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Coordinates> selectCoordinatesByIds(@Param("ids") List<Integer> ids);
}
