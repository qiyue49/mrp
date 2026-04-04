package com.sparkit.mrp.repository;

import com.sparkit.mrp.entity.BOM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BOMRepository extends JpaRepository<BOM, Long> {
    List<BOM> findByProductCode(String productCode);
}
