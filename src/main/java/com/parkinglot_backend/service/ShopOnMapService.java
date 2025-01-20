package com.parkinglot_backend.service;

import com.parkinglot_backend.util.Result;

public interface ShopOnMapService {

    Result getAllShopLocations();

    Result getShopLocationsByFloor(String floor);
}
