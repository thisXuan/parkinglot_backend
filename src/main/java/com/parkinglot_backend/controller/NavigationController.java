package com.parkinglot_backend.controller;

import com.parkinglot_backend.dto.NavigationPoint;
import com.parkinglot_backend.util.Result;
import com.parkinglot_backend.service.NavigationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/getPath")
    public Result getPath(@RequestBody NavigationPoint navigationPoint) {
        return navigationService.getPath(navigationPoint);
    }
}
