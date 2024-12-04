package com.example.handle.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "工程队信息", description = "存储工程队的详细信息")
public class Engineering {
    @ApiModelProperty(value = "自增主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "工程队名称", required = true, example = "第一工程队")
    private String engineeringTeamName;

    @ApiModelProperty(value = "工程队人数", required = true, example = "10")
    private Integer number;

    @ApiModelProperty(value = "负责人姓名", required = true, example = "李四")
    private String sName;

    @ApiModelProperty(value = "负责人联系电话", required = true, example = "13900139000")
    private String sTel;
}
