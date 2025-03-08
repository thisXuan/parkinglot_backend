package com.parkinglot_backend.service;

import com.parkinglot_backend.dto.ChangeNameDTO;
import com.parkinglot_backend.entity.Store;
import com.parkinglot_backend.util.Result;

public interface ManagerService {
    Result changeStoreLocation(String token, Store store);
}
