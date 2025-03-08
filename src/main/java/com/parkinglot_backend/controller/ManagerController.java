package com.parkinglot_backend.controller;

import com.parkinglot_backend.dto.ChangeNameDTO;
import com.parkinglot_backend.dto.NavigationPoint;
import com.parkinglot_backend.service.ManagerService;
import com.parkinglot_backend.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-05
 * @Description:
 */

@Slf4j
@RestController
@RequestMapping("/manager")
@Tag(name = "管理部分")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Operation(summary = "修改店铺位置")
    @PostMapping("/change")
    public Result changeLocation(@RequestHeader("token")String token, @RequestBody ChangeNameDTO changeNameDTO) {
        return null;
    }
}
