package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName Store
 */
@TableName(value ="Store")
@Data
public class Store implements Serializable {
    private Integer id;

    @TableField("storename")
    private String storeName;

    @TableField("servicecategory")
    private String serviceCategory;

    @TableField("servicetype")
    private String serviceType;

    @TableField("businesshours")
    private String businessHours;

    private String address;

    @TableField("floornumber")
    private Integer floorNumber;

    private String description;

    @TableField("recommendedservices")
    private Object recommendedServices;

    private String image;

    private String x;

    private String y;

    private static final long serialVersionUID = 1L;
}