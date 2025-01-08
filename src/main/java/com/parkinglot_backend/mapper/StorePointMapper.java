package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.Store;
import com.parkinglot_backend.entity.StorePoint;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StorePointMapper extends BaseMapper<StorePoint> {
    @Select("SELECT store_id, point_id FROM Store_Point")
    List<StorePoint> getStorePointList();  // 返回 List<StorePoint> 类型的结果
}
