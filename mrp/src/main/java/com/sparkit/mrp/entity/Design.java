package com.sparkit.mrp.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "design")
public class Design {
    @Id
    private String designId;
    private String designCode;
    private String creatorId;
    private String designFile;
    private String specifications;
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
