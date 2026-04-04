package com.sparkit.mrp.repository;

import com.sparkit.mrp.entity.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductionPlanRepository extends JpaRepository<ProductionPlan, String> {
    Optional<ProductionPlan> findByOrderId(String orderId);
}
