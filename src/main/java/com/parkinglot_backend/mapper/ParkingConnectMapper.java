package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.Connection;
import com.parkinglot_backend.entity.ParkingConnect;
import com.parkinglot_backend.entity.Points;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ParkingConnectMapper extends BaseMapper<ParkingConnect> {
    @Select("SELECT point_id1, point_id2 FROM parking_connect")
    List<Connection> selectAllConnections();
}
