package com.parkinglot_backend.controller;

import com.parkinglot_backend.service.VisitorService;
import com.parkinglot_backend.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-03-23
 * @Description:
 */

@Slf4j
@RestController
@RequestMapping("/data")
public class VisitorController {
    @Autowired
    private VisitorService visitorService;

    @Operation(summary = "数据概览")
    @GetMapping("/TotalView")
    public Result getTotalView(@RequestHeader("token")String token){
        return visitorService.getTotalViewService(token);
    }

    @Operation(summary = "销售额分析")
    @GetMapping("/salesAnalysis")
    public Result getSalesAnalysis(@RequestHeader("token")String token){
        return visitorService.getSalesAnalysisService(token);
    }

    @Operation(summary = "销售额分析")
    @GetMapping("/orderAnalysis")
    public Result getOrderAnalysis(@RequestHeader("token")String token){
        return visitorService.getOrderAnalysisService(token);
    }

    @Operation(summary = "用户统计")
    @GetMapping("/userAnalysis")
    public Result getUserAnalysis(@RequestHeader("token")String token){
        return visitorService.getUserAnalysisService(token);
    }
}
