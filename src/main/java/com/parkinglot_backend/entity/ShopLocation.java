package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-01-21
 * @Description:
 */


@TableName(value = "shopLocation")
@Data
public class ShopLocation implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("x")
    private Double x;

    @TableField("y")
    private Double y;

    @TableField("floorNumber")
    private String floorNumber;

    // 其他字段...
}
