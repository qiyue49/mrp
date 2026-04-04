package com.sparkit.mrp.test;

import com.sparkit.mrp.service.EventPublisher;
import com.sparkit.mrp.event.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class RocketMQTest {

    @Autowired
    private EventPublisher eventPublisher;

    @Test
    public void testEventPublishing() {
        // Create test event
        Event event = new Event() { };
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("TEST_EVENT");
        event.setAggregateId(UUID.randomUUID().toString());
        event.setVersion(1L);

        // Publish event
        eventPublisher.publishOrderEvent(event);
        eventPublisher.publishSampleEvent(event);
        eventPublisher.publishProductionEvent(event);
        eventPublisher.publishQcEvent(event);
        eventPublisher.publishLogisticsEvent(event);

        // If no exceptions are thrown, the test passes
        assert true;
    }
}
