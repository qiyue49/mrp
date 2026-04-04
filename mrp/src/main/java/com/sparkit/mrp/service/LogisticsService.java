package com.sparkit.mrp.service;

import com.sparkit.mrp.entity.Logistics;
import com.sparkit.mrp.repository.LogisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class LogisticsService {

    @Autowired
    private LogisticsRepository logisticsRepository;

    @Transactional
    public Logistics createLogistics(Logistics logistics) {
        // 生成UUID
        String logisticsId = UUID.randomUUID().toString();
        logistics.setLogisticsId(logisticsId);
        logistics.setStatus("CREATED");
        logistics.setVersion(1L);
        logistics.setCreatedAt(LocalDateTime.now());
        
        // 保存物流信息
        logisticsRepository.save(logistics);
        log.info("Logistics created: {}", logisticsId);

        return logistics;
    }

    public Logistics getLogisticsById(String logisticsId) {
        return logisticsRepository.findById(logisticsId).orElse(null);
    }

    @Transactional
    public Logistics updateLogisticsStatus(String logisticsId, String status, String updatedBy) {
        Logistics logistics = logisticsRepository.findById(logisticsId).orElse(null);
        if (logistics == null) {
            throw new RuntimeException("Logistics not found: " + logisticsId);
        }

        logistics.setStatus(status);
        logistics.setVersion(logistics.getVersion() + 1);
        logistics.setUpdatedBy(updatedBy);
        logistics.setUpdatedAt(LocalDateTime.now());

        logisticsRepository.save(logistics);
        log.info("Logistics status updated: {} -> {}", logisticsId, status);

        return logistics;
    }

    public Logistics getLogisticsByOrderId(String orderId) {
        return logisticsRepository.findByOrderId(orderId).orElse(null);
    }
}
