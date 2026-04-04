package com.sparkit.mrp.service;

import com.sparkit.mrp.entity.ExternalSystem;
import com.sparkit.mrp.entity.ExternalSystemMapping;
import com.sparkit.mrp.repository.ExternalSystemRepository;
import com.sparkit.mrp.repository.ExternalSystemMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class IntegrationService {

    @Autowired
    private ExternalSystemRepository externalSystemRepository;

    @Autowired
    private ExternalSystemMappingRepository externalSystemMappingRepository;

    @Transactional
    public ExternalSystem registerExternalSystem(ExternalSystem system) {
        // 生成UUID
        String systemId = UUID.randomUUID().toString();
        system.setSystemId(systemId);
        system.setStatus("ACTIVE");
        system.setVersion(1L);
        system.setCreatedAt(LocalDateTime.now());
        
        // 保存外部系统
        externalSystemRepository.save(system);
        log.info("External system registered: {}", systemId);

        return system;
    }

    public ExternalSystem getExternalSystemById(String systemId) {
        return externalSystemRepository.findById(systemId).orElse(null);
    }

    @Transactional
    public ExternalSystemMapping createExternalSystemMapping(ExternalSystemMapping mapping) {
        // 生成UUID
        String mappingId = UUID.randomUUID().toString();
        mapping.setMappingId(mappingId);
        mapping.setStatus("ACTIVE");
        mapping.setVersion(1L);
        mapping.setCreatedAt(LocalDateTime.now());
        
        // 保存外部ID映射
        externalSystemMappingRepository.save(mapping);
        log.info("External system mapping created: {}", mappingId);

        return mapping;
    }

    public ExternalSystemMapping getExternalSystemMapping(String systemId, String entityType, String internalId) {
        return externalSystemMappingRepository.findBySystemIdAndEntityTypeAndInternalId(systemId, entityType, internalId).orElse(null);
    }

    public ExternalSystemMapping getExternalSystemMappingByExternalId(String systemId, String entityType, String externalId) {
        return externalSystemMappingRepository.findBySystemIdAndEntityTypeAndExternalId(systemId, entityType, externalId).orElse(null);
    }
}
