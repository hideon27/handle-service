package com.example.handle.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "管理员信息", description = "存储管理员的详细信息")
public class Administrators {

    @ApiModelProperty(value = "自增主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "管理员账号", required = true, example = "admin")
    private String aAccount;

    @ApiModelProperty(value = "管理员密码", required = true, example = "admin123")
    private String aPassword;

    @ApiModelProperty(value = "管理员姓名", required = true, example = "张三")
    private String aName;

    @ApiModelProperty(value = "身份证号", required = true, example = "110101199003071234")
    private String aId;

    @ApiModelProperty(value = "电子邮箱", required = true, example = "admin@example.com")
    private String aEmail;

    @ApiModelProperty(value = "联系电话", required = true, example = "13800138000")
    private String aTel;

    @ApiModelProperty(value = "管理员级别", required = true, example = "1")
    private Integer aLevel;

    @ApiModelProperty(value = "最后登录时间", example = "2023-12-25 15:30:00")
    private Timestamp lastLoginTime;
}
