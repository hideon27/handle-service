package com.example.handle.controller;

import com.example.handle.dto.ApiResponse;
import com.example.handle.dto.resultdata.EngineeringDTO;
import com.example.handle.dto.resultdata.StratumDTO;
import com.example.handle.model.Stratums;
import com.example.handle.service.StratumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Api(tags = "岩芯信息接口")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class StratumController {
    @Autowired
    private StratumService stratumService;

    @ApiOperation("从数据库列出工程队名")
    @GetMapping("/getEngineeringTeamName")
    public ApiResponse<?> getEngineeringTeamName() {
        List<EngineeringDTO> data = stratumService.getEngineeringTeamName();
        return ApiResponse.success(Collections.singletonMap("result", data));
    }

    @ApiOperation("添加图片时从岩芯表获得岩芯信息")
    @GetMapping("/getStratumName")
    public ApiResponse<?> getStratumName() {
        List<StratumDTO> data = stratumService.getStratumName();
        return ApiResponse.success(Collections.singletonMap("result", data));
    }

    @ApiOperation("获得地层信息")
    @GetMapping("/change/showStratumInfo")
    public ApiResponse<?> showStratumInfo(@RequestParam String stratumId) {
        List<Stratums> result = stratumService.getStratumInfoByName(stratumId);
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("根据输入参数获得地层信息")
    @GetMapping("/get/getStratumInfo")
    public ApiResponse<?> getStratumInfo(@RequestParam(required = false) String stratumId,
                                       @RequestParam(required = false) String stratumName,
                                       @RequestParam(required = false) String stratumLen,
                                       @RequestParam(required = false) String stratumAdd,
                                       @RequestParam(required = false) String stratumPro,
                                       @RequestParam(required = false) String stratumIntegrity) {
        Map<String, Object> params = new HashMap<>();
        params.put("stratum_id", stratumId);
        params.put("stratum_name", stratumName);
        params.put("stratum_len", stratumLen);
        params.put("stratum_add", stratumAdd);
        params.put("stratum_pro", stratumPro);
        params.put("integrity", stratumIntegrity);

        List<Stratums> result = stratumService.getStratumInfoByDynamicParams(params);
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("插入岩柱信息")
    @PostMapping("/post/insertStratum")
    public ApiResponse<?> insertStratum(@RequestBody Map<String, String> receivedData) {
        try {
            int result = stratumService.insertStratumInfo(
                receivedData.get("stratumId"),
                receivedData.get("stratumName"),
                Double.parseDouble(receivedData.get("stratumLen")),
                receivedData.getOrDefault("stratumAdd", "广州"),
                receivedData.getOrDefault("stratumPro", "P2023001")
            );
            if (result == 1) {
                return ApiResponse.success(Collections.singletonMap("result", "插入成功"));
            }
        } catch (Exception e) {
            return ApiResponse.fail("插入失败：" + e.getMessage());
        }
        return ApiResponse.fail("插入失败");
    }

    @ApiOperation("更新地层信息")
    @PostMapping("/change/updateSubmitStratum")
    public ApiResponse<?> updateSubmitStratum(@RequestBody Map<String, String> receivedData) {
        String stratumId = receivedData.get("stratumId");
        if (stratumId == null) {
            return ApiResponse.fail("stratumId不能为空");
        }
        
        String stratumName = receivedData.getOrDefault("stratumName", "");
        double stratumLen = Double.parseDouble(receivedData.getOrDefault("stratumLen", "-1"));
        String stratumAdd = receivedData.getOrDefault("stratumAdd", "");
        String stratumPro = receivedData.getOrDefault("stratumPro", "");
        String stratumIntegrity = receivedData.getOrDefault("stratumIntegrity", "");

        stratumService.updateStratumInfoById(stratumId, stratumName, stratumLen, stratumAdd, stratumPro, stratumIntegrity);
        return ApiResponse.success(Collections.singletonMap("result", "更新成功"));
    }

    @ApiOperation("删除地层")
    @PostMapping("/change/deleteStratum")
    public ApiResponse<?> deleteStratum(@RequestBody Map<String, String> receivedData) {
        stratumService.deleteStratumInfoById(receivedData.get("stratumId"));
        return ApiResponse.success(Collections.singletonMap("result", "删除成功"));
    }

    @ApiOperation("检查岩柱完整性(触发式)")
    @GetMapping("/get/checkStratumIntegrity_trigger")
    public ApiResponse<?> checkStratumIntegrityTrigger(@RequestParam String stratumId) {
        Map<String, Object> result = stratumService.validateStratumIntegrity(stratumId);
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("检查岩柱完整性(非触发式)")
    @GetMapping("/get/checkStratumIntegrity_notrigger")
    public ApiResponse<?> checkStratumIntegrityNoTrigger() {
        List<Map<String, Object>> result = stratumService.checkStratumIntegrityGlobally();
        if (result != null && !result.isEmpty()) {
            return ApiResponse.success(Collections.singletonMap("result", result));
        }
        return ApiResponse.success(Collections.singletonMap("result", "所有岩柱完整"));
    }
} 