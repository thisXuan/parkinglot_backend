package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.dto.StoreDTO;
import com.parkinglot_backend.entity.ShopLocation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ShopLocationMapper extends BaseMapper<ShopLocation> {

    // 查询所有店铺位置信息，包括店铺名称、坐标和楼层编号
    @Select("Select sl.name AS name, " +
            "       sl.x AS x, " +
            "       sl.y AS y, " +
            "       sl.floorNumber AS floorNumber, " +
            "       sl.scale AS scale "+
            "FROM shopLocation sl WHERE ABS(sl.x) > 1E-10 AND ABS(sl.y) > 1E-10")
    List<StoreDTO> selectAllShopLocations();

    // 根据店铺名称查询单个店铺位置信息
    @Select("Select sl.name AS name, " +
            "       sl.x AS x, " +
            "       sl.y AS y, " +
            "       sl.floorNumber AS floorNumber " +
            "FROM shopLocation sl " +
            "WHERE sl.name = #{name}")
    StoreDTO selectShopLocationByName(String name);

    // 根据楼层编号查询所有店铺位置信息
    @Select("Select sl.name AS name, " +
            "       sl.x AS x, " +
            "       sl.y AS y, " +
            "       sl.floorNumber AS floorNumber " +
            "FROM shopLocation sl " +
            "WHERE sl.floorNumber = #{floorNumber}")
    List<StoreDTO> selectShopLocationsByFloorNumber(String floorNumber);

    @Update("UPDATE shopLocation " +
            "SET name = #{newName} " +
            "WHERE name = #{oldName}")
    int updateShopLocationName(@Param("oldName") String oldName, @Param("newName") String newName);
}
