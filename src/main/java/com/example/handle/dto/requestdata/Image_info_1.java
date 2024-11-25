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
@ApiModel("管理员")
public class Image_info_1 {

    @ApiModelProperty(value = "图像名称", required = true, example = "133")
    private String imageName;
    @ApiModelProperty(value = "岩芯ID", required = true, example = "133")
    private String imageSid;
    @ApiModelProperty(value = "段开始", required = true, example = "李四")
    private double imageStart;
    @ApiModelProperty(value = "段深度", required = true, example = "11")
    private double imageEnd;
    @ApiModelProperty(value = "岩芯段类型", required = true, example = "1")
    private String imageType;
    @ApiModelProperty(value = "段深度", required = true, example = "133")
    private double imageDepth;
    @ApiModelProperty(value = "原名", required = true, example = "133")
    private String imageNameOld;

}
