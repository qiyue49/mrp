package com.sparkit.mrp.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "external_system")
public class ExternalSystem {
    @Id
    private String systemId;
    private String systemCode;
    private String systemName;
    private String systemType;
    private String status;
    private String connectionInfo;
    private Long version;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String extJson;
}
