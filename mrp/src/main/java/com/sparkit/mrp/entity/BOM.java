package com.sparkit.mrp.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "bom")
public class BOM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productCode;

    @Column(nullable = false)
    private String materialCode;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private String unit;
}
