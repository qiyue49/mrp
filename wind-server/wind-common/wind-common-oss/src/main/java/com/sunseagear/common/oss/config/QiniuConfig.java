package com.sunseagear.common.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "oss")
public class QiniuConfig {
    private String accessKey = "";
    private String secretKey = "";
    private String bucketName = "";
    private String domain = "";
    private String prefix = "";
}
