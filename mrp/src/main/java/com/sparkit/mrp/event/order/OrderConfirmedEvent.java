package com.sparkit.mrp.event.order;

import com.sparkit.mrp.event.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderConfirmedEvent extends Event {
    private String orderId;
    private String confirmedBy;

    public OrderConfirmedEvent() {
        super();
        this.setEventType("OrderConfirmed");
    }
}
