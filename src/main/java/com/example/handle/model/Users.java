package com.example.handle.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户")
public class Users {

    @ApiModelProperty(value = "用户登录账号", required = true, example = "1")
    private String u_account;
    @ApiModelProperty(value = "工号", required = true, example = "13241")
    private String u_num;
    @ApiModelProperty(value = "密码", required = true, example = "2212")
    private String u_password;
    @ApiModelProperty(value = "姓名", required = true, example = "小四")
    private String u_name;
    @ApiModelProperty(value = "性别", required = true, example = "男")
    private String u_sex;
    @ApiModelProperty(value = "身份证号", required = true, example = "321322152753446614")
    private String u_id;
    @ApiModelProperty(value = "邮箱", required = true, example = "52@edu.com")
    private String u_email;
    @ApiModelProperty(value = "电话号码", required = true, example = "15045625501")
    private String u_tel;
    @ApiModelProperty(value = "所属工程队", required = true, example = "11")
    private String u_et_name;
    @ApiModelProperty(value = "注册时间", required = true, example = "2023:12:1")
    private Timestamp u_regdate;
}
