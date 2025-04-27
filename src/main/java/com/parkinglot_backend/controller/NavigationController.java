package com.parkinglot_backend.controller;

import com.parkinglot_backend.dto.NavigationPoint;
import com.parkinglot_backend.util.Result;
import com.parkinglot_backend.service.NavigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
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
@Tag(name = "导航部分")
public class NavigationController {
    @Autowired
    private NavigationService navigationService;

    @Operation(summary = "输入起始点店铺名进行导航")
    @GetMapping("/getPath")
    public Result getPath(@RequestBody NavigationPoint navigationPoint , @RequestParam int mode) {
        return navigationService.getPath(navigationPoint,mode);
    }


}
