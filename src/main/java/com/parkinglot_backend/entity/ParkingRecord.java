package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName ParkingRecord
 */
@TableName(value ="ParkingRecord")
@Data
public class ParkingRecord implements Serializable {
    private Integer id;

    private String carname;

    private Date entrytime;

    private Date exittime;

  //  @TableField("parkingSpace")
    private String parkingSpace;

    private String payment;

    private static final long serialVersionUID = 1L;
}