package com.example.handle.dto.resultdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("数据返回类")
public class ImageAndStratum {

    @ApiModelProperty(value = "图片名称", required = true, example = "1")
    private String image_name;
    @ApiModelProperty(value = "岩心名称", required = true, example = "1")
    private String stratum_name;
    @ApiModelProperty(value = "段开始", required = true, example = "1")
    private double ima_start;
    @ApiModelProperty(value = "段结束", required = true, example = "1")
    private double ima_end;

}
