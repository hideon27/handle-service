package com.example.handle.model;

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
@ApiModel("管理员")
public class Engineering {

    @ApiModelProperty(value = "工程队名称", required = true, example = "1")
    private String engineering_team_name;
    @ApiModelProperty(value = "人数", required = true, example = "133")
    private int number;
    @ApiModelProperty(value = "主管人姓名", required = true, example = "李四")
    private String s_name;
    @ApiModelProperty(value = "联系电话", required = true, example = "11")
    private String s_tel;


}
