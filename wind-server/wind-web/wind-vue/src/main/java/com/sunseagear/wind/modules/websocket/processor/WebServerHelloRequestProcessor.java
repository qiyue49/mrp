package com.sunseagear.wind.modules.websocket.processor;

import com.sunseagear.common.utils.websocket.processor.JsonProcessor;
import com.sunseagear.common.utils.websocket.processor.SocketMessage;
import com.sunseagear.wind.modules.websocket.ConstantMessageCode;
import com.sunseagear.wind.modules.websocket.WebSocketWebCodeServer;
import com.sunseagear.wind.modules.websocket.message.WebServerHelloAckMessage;
import com.sunseagear.wind.modules.websocket.message.WebServerHelloReqMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@AllArgsConstructor
public class WebServerHelloRequestProcessor extends JsonProcessor<WebServerHelloReqMessage> {

    private WebSocketWebCodeServer webSocketWebCodeServer;


    @Override
    public void process(SocketMessage<WebServerHelloReqMessage> message) {
        WebServerHelloReqMessage webServerHelloReqMessage = message.getEntity();
        WebServerHelloAckMessage webServerHelloAckMessage = new WebServerHelloAckMessage();
        webServerHelloAckMessage.setMessage(webServerHelloReqMessage.getMessage());
        webServerHelloAckMessage.setType(webServerHelloReqMessage.getType());
        webServerHelloAckMessage.setTime(new Date());
        Long userId = message.getUserId();
        webSocketWebCodeServer.send(userId, ConstantMessageCode.SERVER_WEB_HELLO_ACK, webServerHelloAckMessage);
    }


}
