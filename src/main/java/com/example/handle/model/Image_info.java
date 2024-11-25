package com.example.handle.model;

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
public class Image_info {

    @ApiModelProperty(value = "图像编号", required = true, example = "1")
    private int image_id;
    @ApiModelProperty(value = "图像名称", required = true, example = "133")
    private String image_name;
    @ApiModelProperty(value = "图像存储路径", required = true, example = "李四")
    private String image_path;
    @ApiModelProperty(value = "上传时间", required = true, example = "11")
    private Timestamp upload_time;
    @ApiModelProperty(value = "上传者", required = true, example = "1")
    private String uploader_num;
    @ApiModelProperty(value = "岩芯ID", required = true, example = "133")
    private String image_sid;
    @ApiModelProperty(value = "段开始", required = true, example = "李四")
    private double ima_start;
    @ApiModelProperty(value = "段深度", required = true, example = "11")
    private double ima_end;
    @ApiModelProperty(value = "岩芯段类型", required = true, example = "1")
    private String s_type;
    @ApiModelProperty(value = "段深度", required = true, example = "133")
    private double ima_depth ;


}
