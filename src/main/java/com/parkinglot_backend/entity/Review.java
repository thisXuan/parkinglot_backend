package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;


import java.io.Serializable;


import java.util.Date;
import java.util.List;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-04-06
 * @Description:
 */


@TableName(value = "Review", autoResultMap = true)  // 启用自动结果映射
@Data
public class Review implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;

    private Integer userId;

    private Integer rating;

    //@TableField("tags")
    private String tags; // 使用 JSONObject 来存储 JSON 数据

    private String comment;

    private Date time;
}
