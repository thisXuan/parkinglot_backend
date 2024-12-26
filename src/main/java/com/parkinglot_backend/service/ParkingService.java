package com.parkinglot_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parkinglot_backend.entity.ParkingRecord;
import com.parkinglot_backend.util.Result;

public interface ParkingService extends IService<ParkingRecord> {
    Result getParkingTimeAndLocation(String carName);

    Result entryParking(String token, ParkingRecord parkingRecord);

    Result getPayment(String token);

    Result getMyCar(String token);

    Result leaveParking(String token, ParkingRecord parkingRecord);
}
