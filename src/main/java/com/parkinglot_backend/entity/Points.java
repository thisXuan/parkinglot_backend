package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName Points
 */
@TableName(value ="Points")
@Data
public class Points implements Serializable {
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

    // 其他字段...
}