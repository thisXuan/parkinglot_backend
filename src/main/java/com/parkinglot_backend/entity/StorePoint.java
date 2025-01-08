package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-01-08
 * @Description:
 */

@TableName(value ="Store_Point")
@Data
public class StorePoint {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("store_id")
    private Integer storeId;

    @TableField("point_id")
    private Integer pointId;

    private static final long serialVersionUID = 1L;
}
