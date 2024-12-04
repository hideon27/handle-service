package com.example.handle.dto.resultdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "图像地层组合信息", description = "返回图像和对应地层的组合信息")
public class ImageAndStratumDTO {
    @ApiModelProperty(value = "图像名称", example = "海淀区岩心样本1")
    private String imageName;

    @ApiModelProperty(value = "地层名称", example = "第一地层")
    private String stratumName;

    @ApiModelProperty(value = "起始深度", example = "10.500")
    private Double imaStart;

    @ApiModelProperty(value = "结束深度", example = "20.500")
    private Double imaEnd;
} 