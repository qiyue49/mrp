package com.sparkit.mrp.event.sample;

import com.sparkit.mrp.event.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleRequestedEvent extends Event {
    private String sampleId;
    private String sampleCode;
    private String orderId;
}
