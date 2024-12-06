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
@ApiModel(value = "管理员信息", description = "存储管理员的详细信息")
public class Administrators {

    @ApiModelProperty(value = "自增主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "管理员账号", required = true, example = "admin")
    @JsonProperty("aAccount")
    private String aAccount;

    @ApiModelProperty(value = "管理员密码", required = true, example = "admin123")
    @JsonProperty("aPassword")
    private String aPassword;

    @ApiModelProperty(value = "管理员姓名", required = true, example = "张三")
    @JsonProperty("aName")
    private String aName;

    @ApiModelProperty(value = "身份证号", required = true, example = "110101199003071234")
    @JsonProperty("aId")
    private String aId;

    @ApiModelProperty(value = "电子邮箱", required = true, example = "admin@example.com")
    @JsonProperty("aEmail")
    private String aEmail;

    @ApiModelProperty(value = "联系电话", required = true, example = "13800138000")
    @JsonProperty("aTel")
    private String aTel;

    @ApiModelProperty(value = "管理员级别", required = true, example = "1")
    @JsonProperty("aLevel")
    private Integer aLevel;

    @ApiModelProperty(value = "最后登录时间", example = "2023-12-25 15:30:00")
    @JsonProperty("lastLoginTime")
    private Timestamp lastLoginTime;
}
