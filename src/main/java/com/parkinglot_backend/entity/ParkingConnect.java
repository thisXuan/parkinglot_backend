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
 * @Description: 停车场车位之间连接关系
 */

@TableName(value ="parking_connect")
@Data
public class ParkingConnect implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("point_id1")
    private Integer pointId1;

    @TableField("point_id2")
    private Integer pointId2;
}
