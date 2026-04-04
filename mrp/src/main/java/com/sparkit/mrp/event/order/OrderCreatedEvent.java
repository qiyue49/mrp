package com.sparkit.mrp.event.order;

import com.sparkit.mrp.event.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderCreatedEvent extends Event {
    private String orderId;
    private String orderCode;
    private String customerId;
    private String designId;
    private Integer quantity;
    private String deliveryDate;
}
