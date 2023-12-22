package com.sunseagear.common.oss.client;

import com.sunseagear.common.oss.constant.CloudClient;

/**
 * 文件上传Factory
 */
public final class OSSClientFactory {

    public static IOSSClient build(String clientType) {
        IOSSClient ossClient;
        if (CloudClient.CLIENT_LOCAL.getValue().equals(clientType)) {
            ossClient = new LocalClient();
        } else if (CloudClient.CLIENTA_ALIYUN.getValue().equals(clientType)) {
            ossClient = new AliyunOSSClient();
        } else if (CloudClient.CLIENTA_TENCENT.getValue().equals(clientType)) {
            ossClient = new TencentCOS();
        } else if (CloudClient.CLIENTA_QINIU.getValue().equals(clientType)) {
            ossClient = new QiniuOSSClient();
        } else {
            return null;
        }
        return ossClient;
    }

}
