package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.entity.CollectTable;
import com.parkinglot_backend.entity.Store;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CollectTableMapper extends BaseMapper<CollectTable> {
    @Insert("INSERT INTO CollectTable (user_id, store_id) VALUES (#{userId}, #{storeId})")
    int insertUserStore(@Param("userId") Integer userId, @Param("storeId") Integer storeId);

    @Select("SELECT COUNT(*) FROM CollectTable WHERE user_id = #{userId} AND store_id = #{storeId}")
    int selectExistUserStore(@Param("userId") Integer userId, @Param("storeId") Integer storeId);

    @Delete("DELETE FROM CollectTable WHERE user_id = #{userId} AND store_id = #{storeId}")
    int deleteFavoriteStore(@Param("userId") Integer userId, @Param("storeId") Integer storeId);

    @Select("SELECT s.* FROM Store s INNER JOIN CollectTable ct ON s.id = ct.store_id WHERE ct.user_id = #{userId}")
    List<Store> selectAllFavoriteStores(@Param("userId") Integer userId);

}
