package com.parkinglot_backend.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-04-06
 * @Description:
 */

@Data
public class ReviewDTO {
    private Integer rating;
    private List<String> tags;
    private String comment;
}
