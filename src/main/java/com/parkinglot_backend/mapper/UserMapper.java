package com.parkinglot_backend.mapper;

import com.parkinglot_backend.dto.RegistrationCount;
import com.parkinglot_backend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    @Select("SELECT type FROM user WHERE id = #{id}")
    int getUserTypeById(@Param("id") Integer id);

    // 新增：获取总记录数
    @Select("SELECT COUNT(*) FROM user")
    int getTotalUserCount();

    // 新增：统计当日新注册人数
    @Select({
            "SELECT COUNT(*) FROM user",
            "WHERE DATE(create_time) = CURDATE()"
    })
    int getNewUsersToday();

    // 新增：最近七天的人数列表
    @Select({
            "SELECT DATE(create_time) as regDate, COUNT(*) as newUserCount",
            "FROM user",
            "WHERE create_time >= DATE_SUB(NOW(), INTERVAL 7 DAY)",
            "GROUP BY DATE(create_time)"
    })
    List<RegistrationCount> getNewUsersInLastSevenDays();

}




