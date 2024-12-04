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
@ApiModel(value = "岩心段信息", description = "存储岩心图像及其相关信息")
public class CoreSegments {

    @ApiModelProperty(value = "自增主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "图像编号(业务主键)", required = true, example = "IMG2023001")
    private String imageId;

    @ApiModelProperty(value = "图像名称", required = true, example = "海淀区岩心样本1")
    private String imageName;

    @ApiModelProperty(value = "图像存储路径", required = true, example = "/images/core/2023/001.jpg")
    private String imagePath;

    @ApiModelProperty(value = "上传时间", example = "2023-12-25 10:30:00")
    private Timestamp uploadTime;

    @ApiModelProperty(value = "上传者工号", required = true, example = "U2023001")
    private String uploaderNum;

    @ApiModelProperty(value = "段起始深度(米)", required = true, example = "10.500")
    private Double segStart;

    @ApiModelProperty(value = "段结束深度(米)", required = true, example = "20.500")
    private Double segEnd;

    @ApiModelProperty(value = "段长度(米)", required = true, example = "10.000")
    private Double segLen;

    @ApiModelProperty(value = "岩心段类型", required = true, example = "砂岩")
    private String segType;

    @ApiModelProperty(value = "序列号", example = "1")
    private Integer sequenceNo;

    @ApiModelProperty(value = "所属地层编号", required = true, example = "S2023001")
    private String stratumId;

    @ApiModelProperty(value = "地层长度(米)", required = true, example = "100.500")
    private Double stratumLen;

    @ApiModelProperty(value = "最后编辑时间", example = "2023-12-25 15:30:00")
    private Timestamp editTime;
}
