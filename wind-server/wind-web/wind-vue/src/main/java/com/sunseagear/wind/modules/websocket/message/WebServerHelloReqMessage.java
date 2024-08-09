package com.sunseagear.wind.modules.websocket.message;

import com.sunseagear.wind.modules.websocket.ConstantMessageCode;
import lombok.Data;

@Data
public class WebServerHelloReqMessage {
    private Integer type;
    private String message;
}
