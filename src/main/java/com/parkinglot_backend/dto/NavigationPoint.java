package com.parkinglot_backend.dto;

import lombok.Data;

@Data
public class NavigationPoint {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
}
