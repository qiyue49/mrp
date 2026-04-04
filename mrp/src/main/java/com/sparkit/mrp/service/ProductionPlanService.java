package com.sparkit.mrp.service;

import com.sparkit.mrp.entity.ProductionPlan;
import com.sparkit.mrp.event.production.ProductionPlannedEvent;
import com.sparkit.mrp.repository.ProductionPlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class ProductionPlanService {

    @Autowired
    private ProductionPlanRepository productionPlanRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Transactional
    public ProductionPlan createProductionPlan(ProductionPlan plan) {
        // 生成UUID
        String planId = UUID.randomUUID().toString();
        plan.setPlanId(planId);
        plan.setStatus("PLANNED");
        plan.setVersion(1L);
        plan.setCreatedAt(LocalDateTime.now());
        
        // 保存生产计划
        productionPlanRepository.save(plan);
        log.info("Production plan created: {}", planId);

        // 发布生产计划创建事件
        ProductionPlannedEvent event = new ProductionPlannedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setAggregateId(planId);
        event.setVersion(1L);
        event.setSource("production-service");
        event.setPlanId(planId);
        event.setPlanCode(plan.getPlanCode());
        event.setOrderId(plan.getOrderId());
        event.setStartDate(plan.getStartDate());
        event.setEndDate(plan.getEndDate());

        eventPublisher.publishProductionEvent(event);
        log.info("ProductionPlannedEvent published: {}", event.getEventId());

        return plan;
    }

    public ProductionPlan getProductionPlanById(String planId) {
        return productionPlanRepository.findById(planId).orElse(null);
    }

    @Transactional
    public ProductionPlan updateProductionPlanStatus(String planId, String status, String updatedBy) {
        ProductionPlan plan = productionPlanRepository.findById(planId).orElse(null);
        if (plan == null) {
            throw new RuntimeException("Production plan not found: " + planId);
        }

        plan.setStatus(status);
        plan.setVersion(plan.getVersion() + 1);
        plan.setUpdatedBy(updatedBy);
        plan.setUpdatedAt(LocalDateTime.now());

        productionPlanRepository.save(plan);
        log.info("Production plan status updated: {} -> {}", planId, status);

        return plan;
    }

    public ProductionPlan getProductionPlanByOrderId(String orderId) {
        return productionPlanRepository.findByOrderId(orderId).orElse(null);
    }
}
