package com.parkinglot_backend.dto;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-23
 * @Description:
 */

@Data
public class DataDTO {
    private Integer visitor;
    private Integer parking;
    private Integer store;
}
