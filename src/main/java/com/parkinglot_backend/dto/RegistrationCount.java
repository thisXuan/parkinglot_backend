package com.parkinglot_backend.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-23
 * @Description:
 */


@Data
public class RegistrationCount {
    private LocalDate regDate;
    private Integer newUserCount;
}
