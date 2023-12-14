package com.sunseagear.common.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oss.tencent")
public class TencentConfig {
    //API密钥secretId
    private String secretId = "";

    //API密钥secretKey
    private String secretKey = "";

    //存储桶所属地域
    private String region = "";

    //存储桶空间名称
    private String bucketName = "";

    //存储桶访问域名
    private String domain;

    //上传文件前缀路径(eg:/images/) 设置自己的主目录
    private String prefix;


}
