package com.parkinglot_backend.mapper;

import com.parkinglot_backend.dataStructure.Point;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.Points;
import org.apache.ibatis.annotations.Select;
import java.util.List;
public interface PointMapper extends BaseMapper<Points> {
    @Select("SELECT id, x_coordinate, y_coordinate FROM Points")
    List<Points> selectAllCoordinates();

}
