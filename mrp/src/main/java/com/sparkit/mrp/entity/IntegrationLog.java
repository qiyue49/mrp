package com.sparkit.mrp.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "integration_log")
public class IntegrationLog {
    @Id
    private String logId;
    private String systemId;
    private String operationType;
    private String entityType;
    private String entityId;
    private String status;
    private String errorMessage;
    private String requestData;
    private String responseData;
    private LocalDateTime createdAt;
    private String extJson;
}
