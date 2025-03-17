package com.example.handle.service.impl;

import com.example.handle.dto.resultdata.OperationLogResultDTO;
import com.example.handle.mapper.LogMapper;
import com.example.handle.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public void logOperation(Long userId, String operationType, String operationContent, String operationResult) {
        logMapper.insertOperationLog(userId, operationType, operationContent, operationResult);
    }

    @Override
    public List<OperationLogResultDTO> getOperationLogs(Long userId, Integer limit) {
        return logMapper.getOperationLogs(userId, limit);
    }
} 