package com.sparkit.mrp.controller;

import com.sparkit.mrp.event.Event;
import com.sparkit.mrp.service.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {

    private final EventPublisher eventPublisher;

    @PostMapping("/publish/{topic}")
    public String publishEvent(@PathVariable String topic, @RequestParam String message) {
        log.info("Publishing test event to topic: {}", topic);
        
        Event event = new Event() {};
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("TEST_EVENT");
        event.setAggregateId(UUID.randomUUID().toString());
        event.setVersion(1L);
        event.setTimestamp(LocalDateTime.now());
        event.setSource("test-controller");
        
        eventPublisher.publish(topic, event);
        
        return "Event published to topic: " + topic;
    }

    @PostMapping("/publish/order")
    public String publishOrderEvent(@RequestParam String message) {
        log.info("Publishing test order event");
        
        Event event = new Event() {};
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("TEST_ORDER_EVENT");
        event.setAggregateId(UUID.randomUUID().toString());
        event.setVersion(1L);
        event.setTimestamp(LocalDateTime.now());
        event.setSource("test-controller");
        
        eventPublisher.publishOrderEvent(event);
        
        return "Order event published";
    }
}
