package com.sparkit.mrp.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "logistics")
public class Logistics {
    @Id
    private String logisticsId;
    private String logisticsCode;
    private String orderId;
    private String carrier;
    private String trackingNumber;
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
