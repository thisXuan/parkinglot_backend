package com.parkinglot_backend.service;

import com.parkinglot_backend.dto.NavigationPoint;
import com.parkinglot_backend.util.Result;
public interface NavigationService {

    Result getPath(NavigationPoint navigationPoint);

    Result getStoreNames();
}
