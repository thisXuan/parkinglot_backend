package com.parkinglot_backend.mapper;

import com.parkinglot_backend.dataStructure.Point;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.Points;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
public interface PointMapper extends BaseMapper<Points> {
    @Select("SELECT id, x_coordinate, y_coordinate, floor, is_elevator FROM Points")
    List<Points> selectAllCoordinates();

    @Select("SELECT x_coordinate, y_coordinate, floor, is_elevator FROM Points WHERE id = #{id}")
    Points selectCoordinatesById(int id);

    @Select("SELECT id FROM Points WHERE x_coordinate = #{x} AND y_coordinate = #{y}")
    Integer selectIdByCoordinates(@Param("x") double x, @Param("y") double y);



}
