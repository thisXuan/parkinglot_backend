package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName sign_record
 */
@TableName(value ="sign_record")
@Data
public class sign_record implements Serializable {
    private Integer id;

    private Integer userId;

    private String dateMonth;

    private Integer mask;

    private Integer continueSignMonth;

    private static final long serialVersionUID = 1L;
}