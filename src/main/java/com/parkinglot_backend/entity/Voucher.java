package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName Voucher
 */
@TableName(value ="Voucher")
@Data
public class Voucher implements Serializable {
    private Integer id;

    private Integer storeid;

    private String title;

    private String subtitle;

    private String rules;

    private Double payvalue;

    private Integer actualvalue;

    private String image;

    private static final long serialVersionUID = 1L;
}