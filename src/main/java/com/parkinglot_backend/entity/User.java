package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    private Integer id;

    private String name;

    private String password;

    private String phone;

    private String point;

    private Integer type;

    private String create_time;

    private static final long serialVersionUID = 1L;
}