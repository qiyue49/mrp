package com.sunseagear.common.oss.client;

import com.sunseagear.common.oss.constant.Constants;

/**
 * 文件上传Factory
 */
public final class OSSClientFactory {

    public static IOSSClient build(String clientType) {
        IOSSClient ossClient;
        if (Constants.CLIENT_LOCAL.equals(clientType)) {
            ossClient = new LocalClient();
        } else if (Constants.CLIENTA_ALIYUN.equals(clientType)) {
            ossClient = new AliyunOSSClient();
        } else if (Constants.CLIENTA_TENCENT.equals(clientType)) {
            ossClient = new TencentCOS();
        } else if (Constants.CLIENTA_QINIU.equals(clientType)) {
            ossClient = new QiniuOSSClient();
        } else {
            return null;
        }
        return ossClient;
    }

}
