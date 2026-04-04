package com.sparkit.mrp.service;

import com.sparkit.mrp.entity.ProductionTask;
import com.sparkit.mrp.event.production.ProductionTaskCreatedEvent;
import com.sparkit.mrp.repository.ProductionTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Slf4j
@Service
public class ProductionTaskService {

    @Autowired
    private ProductionTaskRepository productionTaskRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Transactional
    public ProductionTask createProductionTask(ProductionTask task) {
        // 生成UUID
        String taskId = UUID.randomUUID().toString();
        task.setTaskId(taskId);
        task.setStatus("CREATED");
        task.setVersion(1L);
        task.setCreatedAt(LocalDateTime.now());
        
        // 保存生产任务
        productionTaskRepository.save(task);
        log.info("Production task created: {}", taskId);

        // 发布生产任务创建事件
        ProductionTaskCreatedEvent event = new ProductionTaskCreatedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setAggregateId(taskId);
        event.setVersion(1L);
        event.setSource("production-service");
        event.setTaskId(taskId);
        event.setTaskCode(task.getTaskCode());
        event.setPlanId(task.getPlanId());
        event.setFactoryId(task.getFactoryId());
        event.setProcessId(task.getProcessId());
        event.setQuantity(task.getQuantity());

        eventPublisher.publishProductionEvent(event);
        log.info("ProductionTaskCreatedEvent published: {}", event.getEventId());

        return task;
    }

    public ProductionTask getProductionTaskById(String taskId) {
        return productionTaskRepository.findById(taskId).orElse(null);
    }

    @Transactional
    public ProductionTask updateProductionTaskStatus(String taskId, String status, String updatedBy) {
        ProductionTask task = productionTaskRepository.findById(taskId).orElse(null);
        if (task == null) {
            throw new RuntimeException("Production task not found: " + taskId);
        }

        task.setStatus(status);
        task.setVersion(task.getVersion() + 1);
        task.setUpdatedBy(updatedBy);
        task.setUpdatedAt(LocalDateTime.now());

        // 根据状态更新相应的时间戳
        if ("ASSIGNED".equals(status)) {
            task.setAssignedAt(LocalDateTime.now());
        } else if ("STARTED".equals(status)) {
            task.setStartedAt(LocalDateTime.now());
        } else if ("COMPLETED".equals(status)) {
            task.setCompletedAt(LocalDateTime.now());
        }

        productionTaskRepository.save(task);
        log.info("Production task status updated: {} -> {}", taskId, status);

        return task;
    }

    public List<ProductionTask> getProductionTasksByPlanId(String planId) {
        return productionTaskRepository.findByPlanId(planId);
    }
}
