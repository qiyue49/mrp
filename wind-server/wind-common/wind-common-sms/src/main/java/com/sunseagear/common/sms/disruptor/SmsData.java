package com.sunseagear.common.sms.disruptor;

import com.sunseagear.common.sms.config.SmsConfigProperties;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class SmsData implements Serializable {
    private String phone;
    private String smsTemplate;
    private transient SmsConfigProperties smsConfigProperties;
    private transient Map<String, Object> datas;

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setSmsConfigProperties(SmsConfigProperties smsConfigProperties) {
        this.smsConfigProperties = smsConfigProperties;
    }

    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }

    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate;
    }
}
