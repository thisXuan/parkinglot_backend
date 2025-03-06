package com.parkinglot_backend.dto;

import lombok.Data;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-05
 * @Description:
 */

@Data
public class ChangeNameDTO {
    private String beforeName;
    private String afterName;
}
