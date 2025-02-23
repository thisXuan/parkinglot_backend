package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-02-23
 * @Description:
 */

@TableName(value = "Order")
@Data
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Integer userId;

    @TableField("voucher_id")
    private Integer voucherId;

    @TableField("time")
    private LocalDateTime time;

    @TableField("pay_value")
    private Double payValue;

    @TableField("type")
    private Integer type;
}
