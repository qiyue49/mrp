package com.sparkit.mrp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reconciliation_batch")
public class ReconciliationBatch {
    @Id
    private String batchId;
    private String batchCode;
    private String reconciliationType;
    private String startDate;
    private String endDate;
    private String status;
    private Integer totalRecords;
    private Integer matchedRecords;
    private Integer unmatchedRecords;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    
    @Column(name = "ext_json", length = 4000)
    private String extJson;
}
