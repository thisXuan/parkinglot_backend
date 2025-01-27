package com.parkinglot_backend.mapper;

import com.parkinglot_backend.entity.sign_record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
* @author minxuan
* @description 针对表【sign_record】的数据库操作Mapper
* @createDate 2025-01-19 20:36:12
* @Entity com.parkinglot_backend.entity.sign_record
*/
public interface sign_recordMapper extends BaseMapper<sign_record> {

    @Select("SELECT * FROM sign_record WHERE user_id = #{userId} AND date_month = #{dateMonth}")
    sign_record getSignRecordByUserIdAndDateMonth(Integer userId, String dateMonth);

    @Update("UPDATE sign_record SET mask = #{mask}, continue_sign_month = #{continueSignMonth} WHERE user_id = #{userId} AND date_month = #{dateMonth}")
    int updateSignRecordByUserIdAndDateMonth(@Param("userId") Integer userId, @Param("dateMonth") String dateMonth, @Param("mask") Integer mask, @Param("continueSignMonth") Integer continueSignMonth);

    @Select("SELECT COUNT(*) FROM sign_record WHERE user_id = #{userId} AND date_month = #{dateMonth}")
    int countRecordsByUserIdAndDateMonth(@Param("userId") Integer userId, @Param("dateMonth") String dateMonth);

    @Select("SELECT mask FROM sign_record WHERE user_id = #{userId} AND date_month = #{dateMonth}")
    Long getSignInDaysByUserId(@Param("userId") Integer userId, @Param("dateMonth") String dateMonth);
}




