package com.sunseagear.common.sms.constant;

public enum CloudClient {
    // 本地
    CLIENT_LOCAL("local"),
    // 阿里云
    CLIENTA_ALIYUN("aliyun"),
    // 腾讯云
    CLIENTA_TENCENT("tencent"),
    // 七牛云
    CLIENTA_QINIU("qiniu"),
    //互易
    CLIENT_HUYI("huyi");

    private String value;

    CloudClient(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CloudClient fromValue(String value) {
        for (CloudClient client : values()) {
            if (client.getValue().equals(value)) {
                return client;
            }
        }
        throw new IllegalArgumentException("Invalid cloud client value: " + value);
    }
}
