package com.sparkit.mrp.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "external_system_mapping")
public class ExternalSystemMapping {
    @Id
    private String mappingId;
    private String systemId;
    private String entityType;
    private String internalId;
    private String externalId;
    private String status;
    private Long version;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String extJson;
}
