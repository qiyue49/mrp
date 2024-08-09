package com.sunseagear.wind.modules.websocket.message;

import com.sunseagear.wind.modules.websocket.ConstantMessageCode;
import lombok.Data;

import java.util.Date;

@Data
public class WebServerHelloAckMessage {
    private Date time;
    private Integer type;
    private String message;
}
