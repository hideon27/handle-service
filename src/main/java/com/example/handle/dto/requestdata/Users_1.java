package com.example.handle.dto.requestdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
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
public class Users_1 {

    @ApiModelProperty(value = "用户登录账号", required = true, example = "1")
    private String username;
    @ApiModelProperty(value = "密码", required = true, example = "2212")
    private String password;

}
