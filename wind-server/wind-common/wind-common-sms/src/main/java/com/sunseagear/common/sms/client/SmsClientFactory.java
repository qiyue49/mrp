package com.sunseagear.common.sms.client;

import com.sunseagear.common.sms.constant.CloudClient;

/**
 * 文件上传Factory
 */
public final class SmsClientFactory {

    public static ISmsClient build(String clientType) {
        if (CloudClient.CLIENTA_ALIYUN.getValue().equals(clientType)) {
            return new AliyunSmsClient();
        } else if (CloudClient.CLIENTA_TENCENT.getValue().equals(clientType)) {
            return new TencentSmsClient();
        } else if (CloudClient.CLIENT_HUYI.getValue().equals(clientType)) {
            return new HuyiSmsClient();
        }
        return null;
    }

}
