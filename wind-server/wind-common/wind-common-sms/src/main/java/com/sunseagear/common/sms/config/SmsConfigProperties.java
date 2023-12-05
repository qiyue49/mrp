package com.sunseagear.common.sms.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.common.oss.config
 * @title:
 * @description: SMS配置 * @date: 2018/4/26 9:43
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@Getter
@ConfigurationProperties(prefix = "sms")
public class SmsConfigProperties {

    //使用的短信网关,aliyun:阿里云
    String smsType = "tencent";
    //签名
    String signName = "";
    //是否开启
    private Boolean open = Boolean.FALSE;

    private AliyunConfigProperties aliyun;

    private HuyiConfigProperties huyi;

    private TencentConfigProperties tencent;

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public void setAliyun(AliyunConfigProperties aliyun) {
        this.aliyun = aliyun;
    }

    public void setHuyi(HuyiConfigProperties huyi) {
        this.huyi = huyi;
    }

    public void setTencent(TencentConfigProperties tencent) {
        this.tencent = tencent;
    }
}
