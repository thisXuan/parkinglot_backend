package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.Visitor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface VisitorMapper extends BaseMapper<Visitor> {
    @Select("SELECT count FROM Visitor WHERE date = #{date}")
    Integer getVisitorCountByDate(@Param("date") String date);

    @Insert("INSERT INTO Visitor (date, count) VALUES (#{date}, #{count}) ON DUPLICATE KEY UPDATE count = #{count}")
    void insertVisitorCount(@Param("date") String date, @Param("count") Integer count);
}
