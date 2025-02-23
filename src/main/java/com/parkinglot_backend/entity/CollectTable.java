package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-02-22
 * @Description:
 */

@TableName("CollectTable")
public class CollectTable implements Serializable {
    @TableId(value = "id", type = IdType.NONE) // 假设id不是自增，需要手动插入
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("store_id")
    private Integer storeId;

    @TableField("created_at")
    private LocalDateTime createdAt;

    private static final long serialVersionUID = 1L;
}
