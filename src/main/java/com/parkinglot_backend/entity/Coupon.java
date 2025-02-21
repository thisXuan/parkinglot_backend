package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-02-19
 * @Description:
 */


@TableName(value = "Coupon")
@Data
public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("store_id")
    private Integer storeId;

    @TableField("title")
    private String title;

    @TableField("rules")
    private String rules;

    @TableField("pay_point")
    private Integer payPoint;

    @TableField("status")
    private Integer status;

    @TableField("image")
    private String image;

    @TableField("sale_count")
    private int saleCount;
}