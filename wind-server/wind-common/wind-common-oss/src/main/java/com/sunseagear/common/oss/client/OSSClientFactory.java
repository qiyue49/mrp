package com.sunseagear.common.oss.client;

/**
 * 文件上传Factory
 */
public final class OSSClientFactory {

    public static final String CLIENT_LOCAL = "local"; //本地
    public static final String CLIENTA_ALIYUN = "aliyun"; //阿里云
    public static final String CLIENTA_TENCENT = "tencent"; //腾讯云
    public static final String CLIENTA_QINIU = "qiniu"; //七牛云

    public static IOSSClient build(String clientType) {
        IOSSClient ossClient;
        if (CLIENT_LOCAL.equals(clientType)) {
            ossClient = new LocalClient();
        } else if (CLIENTA_ALIYUN.equals(clientType)) {
            ossClient = new AliyunOSSClient();
        } else if (CLIENTA_TENCENT.equals(clientType)) {
            ossClient = new TencentCOS();
        } else if (CLIENTA_QINIU.equals(clientType)) {
            ossClient = new QiniuOSSClient();
        } else {
            return null;
        }
        return ossClient;
    }

}
