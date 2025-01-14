package com.example.handle.dto.requestdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "操作日志请求", description = "用户操作日志记录请求")
public class OperationLogDTO {
    @ApiModelProperty(value = "操作人ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "操作类型", required = true, example = "上传图片/删除图片/更新信息")
    private String operationType;

    @ApiModelProperty(value = "操作内容", required = true)
    private String operationContent;

    @ApiModelProperty(value = "操作结果", required = true, example = "SUCCESS/FAIL")
    private String operationResult;
} 