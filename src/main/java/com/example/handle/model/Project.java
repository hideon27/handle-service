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
@ApiModel(value = "项目信息", description = "存储项目的详细信息")
public class Project {
    @ApiModelProperty(value = "自增主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "项目编号", required = true, example = "P2023001")
    @JsonProperty("pId")
    private String pId;

    @ApiModelProperty(value = "项目名称", required = true, example = "海淀区地质勘探项目")
    @JsonProperty("pName")
    private String pName;

    @ApiModelProperty(value = "项目负责人(关联users表u_num)", required = true, example = "U2023001")
    @JsonProperty("pPrincipal")
    private String pPrincipal;

    @ApiModelProperty(value = "负责工程队(关联engineering表)", required = true, example = "第一工程队")
    @JsonProperty("pTeam")
    private String pTeam;

    @ApiModelProperty(value = "项目描述", required = true, example = "海淀区综合地质勘探工程")
    @JsonProperty("pDescription")
    private String pDescription;
}
