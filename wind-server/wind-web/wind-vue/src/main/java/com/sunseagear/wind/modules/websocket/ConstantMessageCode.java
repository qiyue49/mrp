package com.sunseagear.wind.modules.websocket;

/*
 * 消息命名规范：
 * web->server:10
 * server->web:11
 * 业务代码站三位
 * 建议一个模块100的范围
 * req:1
 * ack:2
 * */
public class ConstantMessageCode {
    public static final String WEB_SERVER_HELLO_REQ = "WEB_SERVER_HELLO_REQ";
    public static final String SERVER_WEB_HELLO_ACK = "SERVER_WEB_HELLO_ACK";
}
