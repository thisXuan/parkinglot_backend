package com.parkinglot_backend.dto;

import lombok.Data;

@Data
public class StoreDTO {
    private String name;
    private double x;
    private double y;
    private String floorNumber;
}
