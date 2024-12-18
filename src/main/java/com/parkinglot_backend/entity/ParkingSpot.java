package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-12-17
 * @Description: 停车场点位名称记录
 */

@TableName(value ="ParkingSpots")
@Data
public class ParkingSpot implements Serializable {
    @TableId
    private Integer spotId; // 使用 spotId 作为字段名，与数据库中的 spot_id 对应

    @TableField("spot_name")
    private String spotName; // 使用 spotName 作为字段名，与数据库中的 spot_name 对应

    @TableField("is_occupied")
    private Boolean isOccupied; // 使用 isOccupied 作为字段名，与数据库中的
}
