package com.sparkit.mrp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reconciliation_detail")
public class ReconciliationDetail {
    @Id
    private String detailId;
    private String batchId;
    private String entityType;
    private String internalId;
    private String externalId;
    
    @Column(name = "internal_value_json", length = 4000)
    private String internalValueJson;
    
    @Column(name = "external_value_json", length = 4000)
    private String externalValueJson;
    
    private String status;
    private String differenceReason;
    private LocalDateTime createdAt;
    
    @Column(name = "ext_json", length = 4000)
    private String extJson;
}
