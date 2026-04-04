package com.sparkit.mrp.event.production;

import com.sparkit.mrp.event.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductionPlannedEvent extends Event {
    private String planId;
    private String planCode;
    private String orderId;
    private String startDate;
    private String endDate;
}
