package com.example.handle.mapper;

import com.example.handle.dto.resultdata.OperationLogResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LogMapper {
    void insertOperationLog(@Param("userId") Long userId,
                          @Param("operationType") String operationType,
                          @Param("operationContent") String operationContent,
                          @Param("operationResult") String operationResult);

    List<OperationLogResultDTO> getOperationLogs(@Param("userId") Long userId,
                                               @Param("limit") Integer limit);
} 