package com.parkinglot_backend.mapper;

import com.parkinglot_backend.entity.Voucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author minxuan
* @description 针对表【Voucher】的数据库操作Mapper
* @createDate 2024-12-29 16:08:26
* @Entity com.parkinglot_backend.entity.Voucher
*/
public interface VoucherMapper extends BaseMapper<Voucher> {
    @Select("SELECT stock FROM Voucher WHERE id = #{id}")
    Integer selectStockById(@Param("id") Integer id);

    @Select("SELECT payValue FROM Voucher WHERE id = #{id}")
    Integer selectPayValueById(@Param("id") Integer id);

    @Update("UPDATE Voucher SET stock = stock - 1 WHERE id = #{voucherId} AND stock > 0")
    Integer updateStock(@Param("voucherId") Integer voucherId);
}




