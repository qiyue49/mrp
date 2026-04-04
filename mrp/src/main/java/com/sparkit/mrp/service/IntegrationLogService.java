package com.sparkit.mrp.service;

import com.sparkit.mrp.entity.IntegrationLog;
import com.sparkit.mrp.repository.IntegrationLogRepository;
import com.sparkit.mrp.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class IntegrationLogService {

    @Autowired
    private IntegrationLogRepository integrationLogRepository;

    public void logIntegration(String systemId, String operationType, String entityType, String entityId, 
                              String status, String errorMessage, Object requestData, Object responseData) {
        IntegrationLog integrationLog = new IntegrationLog();
        integrationLog.setLogId(UUID.randomUUID().toString());
        integrationLog.setSystemId(systemId);
        integrationLog.setOperationType(operationType);
        integrationLog.setEntityType(entityType);
        integrationLog.setEntityId(entityId);
        integrationLog.setStatus(status);
        integrationLog.setErrorMessage(errorMessage);
        
        // Convert request/response data to JSON string
        String requestDataStr = convertToJsonString(requestData);
        String responseDataStr = convertToJsonString(responseData);
        
        integrationLog.setRequestData(requestDataStr);
        integrationLog.setResponseData(responseDataStr);
        integrationLog.setCreatedAt(LocalDateTime.now());

        integrationLogRepository.save(integrationLog);
        log.info("Integration log created: {}", integrationLog.getLogId());
    }

    private String convertToJsonString(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof String) {
            return (String) data;
        }
        if (data instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) data;
            return JsonUtil.toJson(map);
        }
        return JsonUtil.toJson(data);
    }

    public void logSuccess(String systemId, String operationType, String entityType, String entityId, 
                          Object requestData, Object responseData) {
        logIntegration(systemId, operationType, entityType, entityId, "SUCCESS", null, requestData, responseData);
    }

    public void logFailure(String systemId, String operationType, String entityType, String entityId, 
                          String errorMessage, Object requestData, Object responseData) {
        logIntegration(systemId, operationType, entityType, entityId, "FAILED", errorMessage, requestData, responseData);
    }
}
