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
@ApiModel("用户")
public class Project {

    @ApiModelProperty(value = "项目ID", required = true, example = "1")
    private String p_id;
    @ApiModelProperty(value = "项目名称", required = true, example = "13")
    private String p_name;
    @ApiModelProperty(value = "项目负责人", required = true, example = "李四")
    private String p_principal;
    @ApiModelProperty(value = "项目负责团队", required = true, example = "11")
    private String p_team;
    @ApiModelProperty(value = "项目描述", required = true, example = "首要")
    private String p_description;


}
