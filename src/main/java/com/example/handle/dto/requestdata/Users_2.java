package com.example.handle.dto.requestdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
public class Users_2 {

    @ApiModelProperty(value = "用户登录账号", required = true, example = "1")
    private String username;
    @ApiModelProperty(value = "工号", required = true, example = "13241")
    private String number;
    @ApiModelProperty(value = "密码", required = true, example = "2212")
    private String password;
    @ApiModelProperty(value = "姓名", required = true, example = "小四")
    private String name;
    @ApiModelProperty(value = "性别", required = true, example = "男")
    private String sex;
    @ApiModelProperty(value = "身份证号", required = true, example = "321322152753446614")
    private String id;
    @ApiModelProperty(value = "邮箱", required = true, example = "52@edu.com")
    private String email;
    @ApiModelProperty(value = "电话号码", required = true, example = "15045625501")
    private String tel;
    @ApiModelProperty(value = "所属工程队", required = true, example = "11")
    private String en;


}
