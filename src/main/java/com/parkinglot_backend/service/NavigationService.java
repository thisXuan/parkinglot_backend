package com.parkinglot_backend.service;

import com.parkinglot_backend.util.Result;
public interface NavigationService {
    /**
     * 根据起点和终点坐标计算路径
     * @param startX 起点X坐标
     * @param startY 起点Y坐标
     * @param endX 终点X坐标
     * @param endY 终点Y坐标
     * @return 路径上的坐标列表
     */
    Result getPath(int startX, int startY, int endX, int endY);
}
