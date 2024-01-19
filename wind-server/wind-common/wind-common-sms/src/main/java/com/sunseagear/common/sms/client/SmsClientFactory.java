package com.sunseagear.common.sms.client;

/**
 * 文件上传Factory
 */
public final class SmsClientFactory {
    private static final String CLIENT_ALIYUN = "aliyun"; //阿里云
    private static final String CLIENT_TENCENT = "tencent"; //腾讯云
    private static final String CLIENT_HUYI = "huyi"; //互易
    public static ISmsClient build(String clientType) {
        if (CLIENT_ALIYUN.equals(clientType)) {
            return new AliyunSmsClient();
        } else if (CLIENT_TENCENT.equals(clientType)) {
            return new TencentSmsClient();
        } else if (CLIENT_HUYI.equals(clientType)) {
            return new HuyiSmsClient();
        }
        return null;
    }

}
