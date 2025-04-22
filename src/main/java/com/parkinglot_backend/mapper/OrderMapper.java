package com.parkinglot_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parkinglot_backend.dto.SalesDataDTO;
import com.parkinglot_backend.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper extends BaseMapper<Order>{

    @Insert("INSERT INTO `Order` (id, user_id, voucher_id, time, pay_value, type,image) " +
            "VALUES (#{id}, #{userId}, #{voucherId}, #{time}, #{payValue}, #{type}, #{image})")
    int insertOrder(@Param("id") Long id,
                    @Param("userId") Integer userId,
                    @Param("voucherId") Integer voucherId,
                    @Param("time") LocalDateTime time,
                    @Param("payValue") Double payValue,
                    @Param("type") Integer type,
                    @Param("image") String image);

    // 根据userId返回所有订单
    @Select("SELECT * FROM `Order` WHERE user_id = #{userId} ORDER BY time DESC")
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

    @Update("UPDATE `Order` SET type = #{type} WHERE id = #{id}")
    int updateOrderType(@Param("id") Long id, @Param("type") Integer type);

    @Select({
            "SELECT CAST(time AS DATE) as saleDate, COUNT(*) as orderCount",
            "FROM `Order`",
            "WHERE time >= DATE_SUB(NOW(), INTERVAL 14 DAY)",
            "GROUP BY CAST(time AS DATE)"
    })
    List<SalesDataDTO> getOrderCountForLastFourteenDays();

    @Select({
            "SELECT COUNT(*) as orderCount",
            "FROM `Order`",
            "WHERE DATE(time) = CURDATE()"
    })
    Integer getTodayOrderCount();

    @Select({
            "SELECT COUNT(*) as orderCount",
            "FROM `Order`",
            "WHERE DATE(time) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)"
    })
    Integer getYesterdayOrderCount();

    @Select({
            "SELECT COUNT(*) as orderCount",
            "FROM `Order`",
            "WHERE time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)"
    })
    Integer getRecentSevenDaysOrderCount();

    @Select({
            "SELECT COUNT(*) as orderCount",
            "FROM `Order`",
            "WHERE time >= DATE_SUB(CURDATE(), INTERVAL 14 DAY)"
    })
    Integer getRecentFourteenDaysOrderCount();
}
