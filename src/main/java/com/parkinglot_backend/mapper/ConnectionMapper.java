package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.Connection;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ConnectionMapper extends BaseMapper<Connection> {

    @Select("SELECT point_id1, point_id2 FROM Connection")
    List<Connection> selectAllConnections();
}
