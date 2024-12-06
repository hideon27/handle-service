package com.example.handle.dto.resultdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "地层信息响应", description = "返回地层的基本信息")
public class StratumDTO {
    @ApiModelProperty(value = "地层名称", example = "第一地层")
    private String stratumName;

    @ApiModelProperty(value = "地层编号", example = "S2023001")
    private String stratumId;

    @ApiModelProperty(value = "地层深度", example = "10.500")
    private Double stratumLen;
} 