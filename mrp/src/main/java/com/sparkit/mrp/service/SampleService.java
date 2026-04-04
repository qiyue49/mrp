package com.sparkit.mrp.service;

import com.sparkit.mrp.entity.Sample;
import com.sparkit.mrp.event.sample.SampleRequestedEvent;
import com.sparkit.mrp.repository.SampleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Transactional
    public Sample createSample(Sample sample) {
        // 生成UUID
        String sampleId = UUID.randomUUID().toString();
        sample.setSampleId(sampleId);
        sample.setStatus("REQUESTED");
        sample.setVersion(1L);
        sample.setCreatedAt(LocalDateTime.now());
        
        // 保存样品
        sampleRepository.save(sample);
        log.info("Sample created: {}", sampleId);

        // 发布样品需求创建事件
        SampleRequestedEvent event = new SampleRequestedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setAggregateId(sampleId);
        event.setVersion(1L);
        event.setSource("sample-service");
        event.setSampleId(sampleId);
        event.setSampleCode(sample.getSampleCode());
        event.setOrderId(sample.getOrderId());

        eventPublisher.publishSampleEvent(event);
        log.info("SampleRequestedEvent published: {}", event.getEventId());

        return sample;
    }

    public Sample getSampleById(String sampleId) {
        return sampleRepository.findById(sampleId).orElse(null);
    }

    @Transactional
    public Sample updateSampleStatus(String sampleId, String status, String updatedBy) {
        Sample sample = sampleRepository.findById(sampleId).orElse(null);
        if (sample == null) {
            throw new RuntimeException("Sample not found: " + sampleId);
        }

        sample.setStatus(status);
        sample.setVersion(sample.getVersion() + 1);
        sample.setUpdatedBy(updatedBy);
        sample.setUpdatedAt(LocalDateTime.now());

        sampleRepository.save(sample);
        log.info("Sample status updated: {} -> {}", sampleId, status);

        return sample;
    }

    public Sample getSampleByOrderId(String orderId) {
        return sampleRepository.findByOrderId(orderId).orElse(null);
    }
}
