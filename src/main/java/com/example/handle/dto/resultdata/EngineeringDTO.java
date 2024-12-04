package com.example.handle.dto.resultdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工程队信息响应", description = "返回工程队的基本信息")
public class EngineeringDTO {
    @ApiModelProperty(value = "工程队名称", example = "第一工程队")
    private String engineeringTeamName;
} 