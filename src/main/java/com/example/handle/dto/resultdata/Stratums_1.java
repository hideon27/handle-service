package com.example.handle.dto.resultdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject:dream_house
 * @BelongsPackage:com.example.dream_house.model
 * @Author:Uestc_Xiye
 * @CreateTime:2023-12-17 16:29:49
 */

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("数据返回类")
public class Stratums_1 {

    @ApiModelProperty(value = "岩芯ID", required = true, example = "1")
    private String stratum_id;
    @ApiModelProperty(value = "岩芯名称", required = true, example = "13")
    private String stratum_name;
}
