package com.parkinglot_backend.mapper;

import com.parkinglot_backend.dataStructure.Point;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.Points;
import org.apache.ibatis.annotations.Select;
import java.util.List;
public interface PointMapper extends BaseMapper<Points> {
    @Select("SELECT x_coordinate, y_coordinate FROM Points")
    List<Points> selectAllCoordinates();

    @Select("SELECT x_coordinate FROM Points WHERE id = #{id}")
    int selectXById(int id);  // 根据id查询坐标，返回一个Points对象

    @Select("SELECT y_coordinate FROM Points WHERE id = #{id}")
    int selectYById(int id);  // 根据id查询坐标，返回一个Points对象
}
