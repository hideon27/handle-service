package com.example.handle.dto.requestdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户登录请求", description = "用户登录时的请求数据")
public class UserLoginDTO {
    @ApiModelProperty(value = "用户账号", required = true, example = "user01")
    private String username;

    @ApiModelProperty(value = "用户密码", required = true, example = "password123")
    private String password;
} 