package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.dto.StoreDTO;
import com.parkinglot_backend.entity.Store;
import com.parkinglot_backend.entity.StorePoint;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StorePointMapper extends BaseMapper<StorePoint> {
    @Select("SELECT store_id, point_id FROM Store_Point")
    List<StorePoint> getStorePointList();  // 返回 List<StorePoint> 类型的结果

    // 查询所有商铺信息，包括商铺名称、坐标和楼层
    @Select("SELECT sp.id AS storePointId, " +
            "       s.storename AS name, " +
            "       p.x_coordinate AS x, " +
            "       p.y_coordinate AS y, " +
            "       p.floor AS floorNumber " +
            "FROM Store_Point sp " +
            "JOIN Store s ON sp.store_id = s.id " +
            "JOIN Points p ON sp.point_id = p.id")
    List<StoreDTO> selectAllStoreDetails();

    // 根据 StorePoint ID 查询单个商铺信息
    @Select("SELECT sp.id AS storePointId, " +
            "       s.storename AS name, " +
            "       p.x_coordinate AS x, " +
            "       p.y_coordinate AS y, " +
            "       s.floornumber AS floorNumber " +
            "FROM Store_Point sp " +
            "JOIN Store s ON sp.store_id = s.id " +
            "JOIN Points p ON sp.point_id = p.id " +
            "WHERE sp.id = #{id}")
    StoreDTO selectStoreDetailsById(int id);
}
