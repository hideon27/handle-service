package com.example.handle.dto.resultdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "地层岩心段信息", description = "包含地层信息和对应的岩心段息")
public class StratumSegmentDTO {
    
    @ApiModelProperty(value = "地层ID")
    private String stratumId;
    
    @ApiModelProperty(value = "地层名称")
    private String stratumName;
    
    @ApiModelProperty(value = "地层长度")
    private Double stratumLen;
    
    @ApiModelProperty(value = "地层添加时间")
    private String stratumAdd;
    
    @ApiModelProperty(value = "图像ID")
    private String imageId;
    
    @ApiModelProperty(value = "图像名称")
    private String imageName;
    
    @ApiModelProperty(value = "图像路径")
    private String imagePath;
    
    @ApiModelProperty(value = "段起始深度")
    private Double segStart;
    
    @ApiModelProperty(value = "段结束深度")
    private Double segEnd;
    
    @ApiModelProperty(value = "段长度")
    private Double segLen;
    
    @ApiModelProperty(value = "段类型")
    private String segType;
    
    @ApiModelProperty(value = "序号")
    private Integer sequenceNo;
} 