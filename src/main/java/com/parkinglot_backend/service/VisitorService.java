package com.parkinglot_backend.service;

import com.parkinglot_backend.util.Result;

public interface VisitorService {
    Result getTotalViewService(String token);

    Result getOrderAnalysisService(String token);

    Result getSalesAnalysisService(String token);
}
