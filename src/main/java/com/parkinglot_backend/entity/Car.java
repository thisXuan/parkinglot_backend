package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName Car
 */
@TableName(value ="Car")
@Data
public class Car implements Serializable {
    private Integer id;

    private String carname;

    private Date updatetime;

    private Integer userid;

    private static final long serialVersionUID = 1L;
}