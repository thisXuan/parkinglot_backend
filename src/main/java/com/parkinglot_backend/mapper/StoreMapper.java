package com.parkinglot_backend.mapper;

import com.parkinglot_backend.entity.Store;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author minxuan
* @description 针对表【Store】的数据库操作Mapper
* @createDate 2024-10-27 11:16:24
* @Entity com.parkinglot_backend.entity.Store
*/
public interface StoreMapper extends BaseMapper<Store> {
    @Select("SELECT * FROM Store WHERE StoreName LIKE CONCAT('%', #{name}, '%')")
    List<Store> searchStore(String name);

    @Select("SELECT * FROM Store")
    List<Store> findAllStores();

    @Select("SELECT * FROM Store WHERE ServiceCategory = #{category}")
    List<Store> findStoresByCategory(@Param("category") String category);

    @Select("SELECT StoreName\n" +
            "FROM Store,Store_Point\n" +
            "WHERE Store.id = Store_Point.store_id")
    List<String> findAllStoreNames();

    @Select("SELECT point_id FROM Store_Point WHERE store_id=#{storeId}")
    Integer findPointIdByStoreId(Integer storeId);

    @Select("SELECT * FROM Store " +
            "WHERE (#{category} = '全部' OR servicecategory = #{category}) " +
            "AND (#{floor} = '全部楼层' OR floornumber = #{floor})")
    List<Store> getStoresByFilters(@Param("category") String category,
                                   @Param("floor") String floor);

}




