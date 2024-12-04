package com.example.handle.dto.requestdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "管理员登录请求", description = "管理员登录时的请求数据")
public class AdminLoginDTO {
    @ApiModelProperty(value = "管理员账号", required = true, example = "admin")
    private String username;

    @ApiModelProperty(value = "管理员密码", required = true, example = "admin123")
    private String password;
} 