package com.example.handle.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private String errormsg;
    private T data;
    private Boolean success;

    // 静态方法：创建成功的响应
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(null, null, true);
    }

    // 静态方法：创建成功的响应
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(null, data, true);
    }

    // 静态方法：创建失败的响应
    public static <T> ApiResponse<T> fail(String errormsg) {
        return new ApiResponse<>(errormsg, null, false);
    }
} 