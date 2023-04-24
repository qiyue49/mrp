package com.sunseagear.common.sms.client;

import com.sunseagear.common.sms.constant.Constants;

/**
 * 文件上传Factory
 */
public final class SmsClientFactory {

    public static ISmsClient build(String clientType) {
        if (Constants.CLIENT_ALIYUN.equals(clientType)) {
            return new AliyunSmsClient();
        } else if (Constants.CLIENT_TENCENT.equals(clientType)) {
            return new TencentSmsClient();
        } else if (Constants.CLIENT_HUYI.equals(clientType)) {
            return new HuyiSmsClient();
        }
        return null;
    }

}
