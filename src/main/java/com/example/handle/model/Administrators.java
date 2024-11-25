package com.example.handle.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject:dream_house
 * @BelongsPackage:com.example.dream_house.model
 * @Author:Uestc_Xiye
 * @CreateTime:2023-12-17 16:29:49
 */

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("管理员")
public class Administrators {

    @ApiModelProperty(value = "管理员账号", required = true, example = "1")
    private String a_account;
    @ApiModelProperty(value = "密码", required = true, example = "133")
    private String a_password;
    @ApiModelProperty(value = "姓名", required = true, example = "李四")
    private String a_name;
    @ApiModelProperty(value = "身份证号", required = true, example = "11")
    private String a_id;
    @ApiModelProperty(value = "邮箱", required = true, example = "11@edu.com")
    private String a_email;
    @ApiModelProperty(value = "电话号码", required = true, example = "11")
    private String a_tel;
    @ApiModelProperty(value = "管理员级别", required = true, example = "1")
    private int a_level;

}
