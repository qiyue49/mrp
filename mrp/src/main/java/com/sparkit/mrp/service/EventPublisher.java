package com.sparkit.mrp.service;

import com.sparkit.mrp.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class EventPublisher {

    @Autowired(required = false)
    private RocketMQTemplate rocketMQTemplate;

    public void publish(String topic, Event event) {
        Optional.ofNullable(rocketMQTemplate).ifPresentOrElse(
            template -> {
                try {
                    log.info("Publishing event: {} to topic: {}", event.getEventType(), topic);
                    template.convertAndSend(topic, event);
                    log.info("Event published successfully: {}", event.getEventId());
                } catch (Exception e) {
                    log.error("Failed to publish event: {}", event.getEventId(), e);
                    // Don't throw exception, just log error
                }
            },
            () -> log.warn("RocketMQ template not available, skipping event publishing: {}", event.getEventId())
        );
    }

    // 发布订单事件
    public void publishOrderEvent(Event event) {
        publish("orders-events", event);
    }

    // 发布样品事件
    public void publishSampleEvent(Event event) {
        publish("samples-events", event);
    }

    // 发布生产事件
    public void publishProductionEvent(Event event) {
        publish("production-events", event);
    }

    // 发布QC事件
    public void publishQcEvent(Event event) {
        publish("qc-events", event);
    }

    // 发布物流事件
    public void publishLogisticsEvent(Event event) {
        publish("logistics-events", event);
    }
}
