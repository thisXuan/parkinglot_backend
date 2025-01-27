package com.parkinglot_backend.mapper;

import com.parkinglot_backend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author minxuan
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-10-17 16:37:50
* @Entity com.parkinglot_backend.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT point FROM user WHERE id = #{id}")
    Integer getUserPointByUserId(@Param("id") Integer userId);

    @Update("UPDATE user SET point = #{point} WHERE id = #{userId}")
    void updatePointByUserId(@Param("userId") Integer userId, @Param("point") Integer point);
}




