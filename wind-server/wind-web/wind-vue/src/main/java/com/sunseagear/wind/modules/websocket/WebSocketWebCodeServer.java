package com.sunseagear.wind.modules.websocket;

import com.sunseagear.common.utils.websocket.WebSocketServer;
import com.sunseagear.wind.modules.websocket.processor.WebServerHelloRequestProcessor;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/json/web/server/{userId}")
public class WebSocketWebCodeServer extends WebSocketServer {


    public WebSocketWebCodeServer() {
        onlineSessions = webOnlineSessions;
    }


    @Override
    public void registerMessage() {
        getMessageDispatcher().register(ConstantMessageCode.WEB_SERVER_HELLO_REQ, new WebServerHelloRequestProcessor(this));

    }
}
