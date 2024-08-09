package com.sunseagear.common.utils.websocket.processor;

import lombok.Data;

import java.io.Serializable;

@Data
public class SocketMessage<T> {

    public static final String ERROR_CODE = "-1";
    public static final String OPEN_CODE = "1";
    public static final String CLOSE_CODE = "2";


    private String code;
    protected boolean success;
    protected String message;
    protected Long userId;
    protected String rawData; //接收消息用，接受到的原始数据
    protected Object data;  //发送消息用，数据载体
    protected T entity;    //接收消息用，解析后的数据
}
