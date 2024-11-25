package com.example.handle.dto.resultdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("数据返回类")
public class Engineering_1 {

    @ApiModelProperty(value = "工程队名称", required = true, example = "1")
    private String engineering_team_name;


}
