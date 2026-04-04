package com.sparkit.mrp.event;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public abstract class Event {
    private String eventId;
    private String eventType;
    private String aggregateId;
    private Long version;
    private LocalDateTime timestamp;
    private String source;
    private String correlationId;
    private String causationId;

    public Event() {
        this.timestamp = LocalDateTime.now();
    }
}
