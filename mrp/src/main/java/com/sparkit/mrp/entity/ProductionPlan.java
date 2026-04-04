package com.sparkit.mrp.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "production_plan")
public class ProductionPlan {
    @Id
    private String planId;
    private String planCode;
    private String orderId;
    private String startDate;
    private String endDate;
    private String status;
    private String eventId;
    private Long version;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String sourceSys;
    private String extJson;
}
