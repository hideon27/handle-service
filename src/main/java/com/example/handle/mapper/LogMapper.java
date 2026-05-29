package com.example.handle.mapper;

import com.example.handle.dto.resultdata.OperationLogResultDTO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface LogMapper {
    @Insert("INSERT INTO user_operation_logs (user_id, operation_type, operation_content, operation_result) " +
            "VALUES (#{userId}, #{operationType}, #{operationContent}, #{operationResult})")
    void insertOperationLog(@Param("userId") Long userId,
                          @Param("operationType") String operationType,
                          @Param("operationContent") String operationContent,
                          @Param("operationResult") String operationResult);

    @Select("<script>" +
            "SELECT id, user_id AS userId, operation_type AS operationType, operation_content AS operationContent, " +
            "operation_result AS operationResult, operation_time AS operationTime " +
            "FROM user_operation_logs " +
            "<where>" +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "</where>" +
            "ORDER BY operation_time DESC " +
            "LIMIT #{limit}" +
            "</script>")
    List<OperationLogResultDTO> getOperationLogs(@Param("userId") Long userId,
                                               @Param("limit") Integer limit);
} 
