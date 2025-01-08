package com.parkinglot_backend.dto;

import com.parkinglot_backend.dataStructure.Point;
import lombok.Data;

import java.util.List;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-01-08
 * @Description:
 */

@Data
public class ResultPointDTO {
    private List<Point> filteredPath;
    private List<String> storeNames;
}
