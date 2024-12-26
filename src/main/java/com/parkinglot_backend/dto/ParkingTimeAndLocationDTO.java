package com.parkinglot_backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ParkingTimeAndLocationDTO {
    private Date parkingTime;
    private String location;
}
