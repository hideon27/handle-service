package com.example.handle.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户")
public class Stratums {

    @ApiModelProperty(value = "岩芯ID", required = true, example = "1")
    private String stratum_id;
    @ApiModelProperty(value = "岩芯名称", required = true, example = "13")
    private String stratum_name;
    @ApiModelProperty(value = "岩芯长度", required = true, example = "321")
    private double stratum_len;
    @ApiModelProperty(value = "岩芯地址", required = true, example = "一楼")
    private String stratum_add;
    @ApiModelProperty(value = "所属项目", required = true, example = "11")
    private String stratum_pro;
    @ApiModelProperty(value = "完整性", required = true, example = "NO")
    private String integrity;

}
