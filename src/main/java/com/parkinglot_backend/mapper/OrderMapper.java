package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.dto.SalesDataDTO;
import com.parkinglot_backend.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper extends BaseMapper<Order>{

    @Insert("INSERT INTO `Order` (id, user_id, voucher_id, time, pay_value, type) " +
            "VALUES (#{id}, #{userId}, #{voucherId}, #{time}, #{payValue}, #{type})")
    int insertOrder(@Param("id") Long id,
                    @Param("userId") Integer userId,
                    @Param("voucherId") Integer voucherId,
                    @Param("time") LocalDateTime time,
                    @Param("payValue") Double payValue,
                    @Param("type") Integer type);

    // 根据userId返回所有订单
    @Select("SELECT * FROM `Order` WHERE user_id = #{userId}")
    List<Order> selectOrdersByUserId(@Param("userId") Integer userId);

    // 根据userId和type返回订单
    @Select("SELECT * FROM `Order` WHERE user_id = #{userId} AND type = #{type}")
    List<Order> selectOrdersByUserIdAndType(@Param("userId") Integer userId, @Param("type") Integer type);

    @Select({
            "SELECT CAST(time AS DATE) as saleDate, SUM(pay_value) as totalSales",
            "FROM `Order`",
            "WHERE time >= DATE_SUB(NOW(), INTERVAL 7 DAY)",
            "GROUP BY CAST(time AS DATE)"
    })
    List<SalesDataDTO> getSalesDataForLastSevenDays();
}
