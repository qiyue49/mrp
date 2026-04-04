package com.sparkit.mrp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "qc_record")
public class QcRecord {
    @Id
    private String qcId;
    private String qcCode;
    private String taskId;
    private String inspectorId;
    private String result;
    private String notes;
    private String eventId;
    private Long version;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String sourceSys;
    
    @Column(name = "ext_json", length = 4000)
    private String extJson;
}
