package com.sparkit.mrp.service;

import com.sparkit.mrp.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RocketMQConsumerService {

    @RocketMQMessageListener(topic = "orders-events", consumerGroup = "mrp-consumer-group")
    public static class OrderEventListener implements RocketMQListener<Event> {
        @Override
        public void onMessage(Event event) {
            log.info("Consumed order event: {}", event.getEventId());
            // Process order event
        }
    }

    @RocketMQMessageListener(topic = "samples-events", consumerGroup = "mrp-consumer-group")
    public static class SampleEventListener implements RocketMQListener<Event> {
        @Override
        public void onMessage(Event event) {
            log.info("Consumed sample event: {}", event.getEventId());
            // Process sample event
        }
    }

    @RocketMQMessageListener(topic = "production-events", consumerGroup = "mrp-consumer-group")
    public static class ProductionEventListener implements RocketMQListener<Event> {
        @Override
        public void onMessage(Event event) {
            log.info("Consumed production event: {}", event.getEventId());
            // Process production event
        }
    }

    @RocketMQMessageListener(topic = "qc-events", consumerGroup = "mrp-consumer-group")
    public static class QcEventListener implements RocketMQListener<Event> {
        @Override
        public void onMessage(Event event) {
            log.info("Consumed QC event: {}", event.getEventId());
            // Process QC event
        }
    }

    @RocketMQMessageListener(topic = "logistics-events", consumerGroup = "mrp-consumer-group")
    public static class LogisticsEventListener implements RocketMQListener<Event> {
        @Override
        public void onMessage(Event event) {
            log.info("Consumed logistics event: {}", event.getEventId());
            // Process logistics event
        }
    }
}
