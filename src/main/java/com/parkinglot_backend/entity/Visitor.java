package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-23
 * @Description:
 */

@TableName(value ="Visitor")
@Data
public class Visitor {
    private String date;
    private Integer count;
}
