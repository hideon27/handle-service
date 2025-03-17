package com.example.handle.service;

import com.example.handle.dto.resultdata.OperationLogResultDTO;
import java.util.List;

public interface LogService {
    void logOperation(Long userId, String operationType, String operationContent, String operationResult);
    List<OperationLogResultDTO> getOperationLogs(Long userId, Integer limit);
} 