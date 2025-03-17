package com.example.handle.controller;

import com.example.handle.dto.ApiResponse;
import com.example.handle.dto.resultdata.OperationLogResultDTO;
import com.example.handle.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Api(tags = "操作日志接口")
@RestController
@RequestMapping("/log")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LogController {
    @Autowired
    private LogService logService;

    @ApiOperation(value = "记录用户操作日志")
    @PostMapping("/operation")
    public ApiResponse<?> logOperation(@RequestBody Map<String, Object> logDTO) {
        try {
            logService.logOperation(
                Long.parseLong(logDTO.get("userId").toString()),
                (String) logDTO.get("operationType"),
                (String) logDTO.get("operationContent"),
                (String) logDTO.get("operationResult")
            );
            return ApiResponse.success(Collections.singletonMap("result", "日志记录成功"));
        } catch (Exception e) {
            return ApiResponse.fail("日志记录失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "查询用户操作日志")
    @GetMapping("/query")
    public ApiResponse<?> queryOperationLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false, defaultValue = "5") Integer limit) {
        try {
            List<OperationLogResultDTO> logs = logService.getOperationLogs(userId, limit);
            return ApiResponse.success(Collections.singletonMap("result", logs));
        } catch (Exception e) {
            return ApiResponse.fail("查询日志失败: " + e.getMessage());
        }
    }
} 