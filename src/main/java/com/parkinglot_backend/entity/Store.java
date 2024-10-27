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

    private String storename;

    private String servicecategory;

    private String servicetype;

    private String businesshours;

    private String address;

    private Integer floornumber;

    private String description;

    private Object recommendedservices;

    private static final long serialVersionUID = 1L;
}