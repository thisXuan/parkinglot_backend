package com.parkinglot_backend.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-05
 * @Description:
 */

@Data
public class Coordinates implements Serializable {
    private Double xCoordinate;
    private Double yCoordinate;

    private static final long serialVersionUID = 1L;
}
