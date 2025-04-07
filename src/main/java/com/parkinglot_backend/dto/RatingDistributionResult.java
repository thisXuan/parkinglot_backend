package com.parkinglot_backend.dto;

import lombok.Data;

import java.util.Map;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-04-06
 * @Description:
 */

@Data
public class RatingDistributionResult {
    private Integer rating;
    private Integer count;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
