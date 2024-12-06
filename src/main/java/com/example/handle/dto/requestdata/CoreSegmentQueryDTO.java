package com.example.handle.dto.requestdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "岩芯查询请求", description = "查询岩心段信息的请求参数")
public class CoreSegmentQueryDTO {

    @ApiModelProperty(value = "图像名称", example = "IMG2023001")
    private String imageName;

    @ApiModelProperty(value = "起始深度", example = "10.500")
    private Double segStart;

    @ApiModelProperty(value = "结束深度", example = "20.500")
    private Double segEnd;

    @ApiModelProperty(value = "段深度", example = "15.000")
    private Double segLen;

    @ApiModelProperty(value = "岩芯类型", example = "砂岩")
    private String segType;

    @ApiModelProperty(value = "岩柱ID", example = "1")
    private String stratumId;

    @ApiModelProperty(value = "上传人工号", example = "1234567890")
    private String uploaderNum;
} 