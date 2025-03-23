package com.parkinglot_backend.dto;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

import java.util.List;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-23
 * @Description:
 */

@Data
public class UserAnalyticsDTO {
    private Integer totalRegister;
    private Integer todayRegister;
    private List<Integer> weekRegister;
}
