package com.sparkit.mrp.event.production;

import com.sparkit.mrp.event.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductionTaskCreatedEvent extends Event {
    private String taskId;
    private String taskCode;
    private String planId;
    private String factoryId;
    private String processId;
    private Integer quantity;
}
