package com.parkinglot_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-12-24
 * @Description:
 */


@Data
@TableName(value = "QRCodeScans")
public class QRCodeScans implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "scan_id", type = IdType.AUTO)
    private Integer scanId;

    @TableField("user_id")
    private Integer userId;

    @TableField("qr_content")
    private String qrContent;

    @TableField("scan_time")
    private LocalDateTime scanTime;

    // 其他字段和方法...
}