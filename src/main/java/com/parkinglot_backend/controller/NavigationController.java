package com.parkinglot_backend.controller;

import com.parkinglot_backend.util.Result;
import com.parkinglot_backend.service.NavigationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-11-07
 * @Description:
 */

@Slf4j
@RestController
@RequestMapping("/navigation")
public class NavigationController {
    @Autowired
    private NavigationService navigationService;

    /**
     * 获取路径信息
     *
     * @param startX 起点X坐标
     * @param startY 起点Y坐标
     * @param endX   终点X坐标
     * @param endY   终点Y坐标
     * @return 路径上的坐标列表
     */
    @GetMapping("/getPath")
    public Result getPath(@RequestParam int startX, @RequestParam int startY,
                          @RequestParam int endX, @RequestParam int endY) {
        return navigationService.getPath(startX, startY, endX, endY);
    }
}
