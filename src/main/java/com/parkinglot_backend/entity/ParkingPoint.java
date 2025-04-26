package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-12-17
 * @Description: 类似于Points, 是记录停车场点位信息的
 */

@TableName(value ="parking_lot")
@Data
public class ParkingPoint implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("x_coordinate")
    private Double xCoordinate;

    @TableField("y_coordinate")
    private Double yCoordinate;

    @TableField("floor")
    private String floor;

    @TableField("is_elevator")
    private Integer isElevator;

}
