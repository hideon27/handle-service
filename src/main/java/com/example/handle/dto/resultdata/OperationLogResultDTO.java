package com.example.handle.dto.resultdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "操作日志结果", description = "用户操作日志查询结果")
public class OperationLogResultDTO {
    @ApiModelProperty(value = "日志ID")
    private Long id;

    @ApiModelProperty(value = "操作人ID")
    private Long userId;

    @ApiModelProperty(value = "操作类型")
    private String operationType;

    @ApiModelProperty(value = "操作内容")
    private String operationContent;

    @ApiModelProperty(value = "操作结果")
    private String operationResult;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime;
} 