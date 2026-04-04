package com.sparkit.mrp.repository;

import com.sparkit.mrp.entity.ProductionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductionTaskRepository extends JpaRepository<ProductionTask, String> {
    List<ProductionTask> findByPlanId(String planId);
}
