package com.example.handle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户信息", description = "存储用户的详细信息")
public class Users {
    @ApiModelProperty(value = "自增主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "用户账号", required = true, example = "user01")
    //jackson和lombok的问题
    @JsonProperty("uAccount")
    private String uAccount;

    @ApiModelProperty(value = "用户工号", required = true, example = "U1001")
    @JsonProperty("uNum")
    private String uNum;

    @ApiModelProperty(value = "用户密码", required = true, example = "password123")
    @JsonProperty("uPassword")
    private String uPassword;

    @ApiModelProperty(value = "用户姓名", required = true, example = "王五")
    @JsonProperty("uName")
    private String uName;

    @ApiModelProperty(value = "性别", required = true, example = "男")
    @JsonProperty("uSex")
    private String uSex;

    @ApiModelProperty(value = "身份证号", required = true, example = "110101199003071235")
    @JsonProperty("uId")
    private String uId;

    @ApiModelProperty(value = "电子邮箱", required = true, example = "user01@example.com")
    @JsonProperty("uEmail")
    private String uEmail;

    @ApiModelProperty(value = "联系电话", required = true, example = "13800138001")
    @JsonProperty("uTel")
    private String uTel;

    @ApiModelProperty(value = "所属工程队名称", required = true, example = "第一工程队")
    @JsonProperty("uEtName")
    private String uEtName;

    @ApiModelProperty(value = "注册时间", example = "2023-12-25 10:30:00")
    @JsonProperty("uRegdate")
    private Timestamp uRegdate;

    @ApiModelProperty(value = "最后登录时间", example = "2023-12-25 15:30:00")
    @JsonProperty("lastLoginTime")
    private Timestamp lastLoginTime;
}
