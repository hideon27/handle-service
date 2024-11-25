package com.example.handle.dto.resultdata;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("数据返回类")
public class Json {
    private int code;
    private String message;
    private Object data;
}
