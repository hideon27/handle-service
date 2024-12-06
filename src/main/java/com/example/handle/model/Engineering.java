package com.example.handle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("engineeringTeamName")
    private String engineeringTeamName;

    @ApiModelProperty(value = "工程队人数", required = true, example = "10")
    @JsonProperty("number")
    private Integer number;

    @ApiModelProperty(value = "负责人姓名", required = true, example = "李四")
    @JsonProperty("sName")
    private String sName;

    @ApiModelProperty(value = "负责人联系电话", required = true, example = "13900139000")
    @JsonProperty("sTel")
    private String sTel;
}
