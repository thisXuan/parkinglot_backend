package com.parkinglot_backend.service;

import com.parkinglot_backend.dto.NavigationPoint;
import com.parkinglot_backend.util.Result;

import java.util.List;

public interface NavigationService {

    Result getPath(NavigationPoint navigationPoint);

    //List<String> getStoreNames();
}
