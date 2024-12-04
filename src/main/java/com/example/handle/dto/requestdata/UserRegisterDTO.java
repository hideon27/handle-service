package com.example.handle.dto.requestdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户注册请求", description = "用户注册时的请求数据")
public class UserRegisterDTO {
    @ApiModelProperty(value = "用户账号", required = true, example = "user01")
    private String username;

    @ApiModelProperty(value = "用户工号", required = true, example = "U1001")
    private String number;

    @ApiModelProperty(value = "用户密码", required = true, example = "password123")
    private String password;

    @ApiModelProperty(value = "用户姓名", required = true, example = "王五")
    private String name;

    @ApiModelProperty(value = "性别", required = true, example = "男")
    private String sex;

    @ApiModelProperty(value = "身份证号", required = true, example = "110101199003071235")
    private String id;

    @ApiModelProperty(value = "电子邮箱", required = true, example = "user01@example.com")
    private String email;

    @ApiModelProperty(value = "联系电话", required = true, example = "13800138001")
    private String tel;

    @ApiModelProperty(value = "所属工程队", required = true, example = "第一工程队")
    private String en;
} 