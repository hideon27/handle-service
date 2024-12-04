package com.example.handle.dto.requestdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "岩心段查询请求", description = "查询岩心段信息的请求参数")
public class CoreSegmentQueryDTO {
    @ApiModelProperty(value = "图像编号", example = "IMG2023001")
    private String imageSid;

    @ApiModelProperty(value = "起始深度", example = "10.500")
    private Double imageStart;

    @ApiModelProperty(value = "结束深度", example = "20.500")
    private Double imageEnd;

    @ApiModelProperty(value = "段深度", example = "15.000")
    private Double imageDepth;

    @ApiModelProperty(value = "岩心段类型", example = "砂岩")
    private String imageType;
} 