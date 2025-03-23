package com.parkinglot_backend.dto;

import lombok.Data;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-23
 * @Description:
 */


@Data
public class OrderDTO {
    private Integer todayOrder; // 访问人数（可以使用Redis的Set，记得MySQL和Redis数据同步）
    private Integer dayOrderDay; // 订单日环比（在[0,100]区间）
    private Integer weekOrder; // 周订单数量
    private Integer weekOrderWeek; // 订单周同比（在[0,100]区间）
}