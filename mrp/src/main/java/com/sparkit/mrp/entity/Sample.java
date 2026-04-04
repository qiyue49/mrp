package com.sparkit.mrp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sample")
public class Sample {
    @Id
    private String sampleId;
    private String sampleCode;
    private String orderId;
    private String factoryId;
    private String status;
    
    @Column(name = "result_json", length = 4000)
    private String resultJson;
    
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
