package com.parkinglot_backend.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-04-06
 * @Description:
 */

@Data
public class ReviewWithUser {
    //private Integer reviewId;
    private String reviewer;
    private Integer rating;
    private String tags;
    private String comment;
    private Date time;
}