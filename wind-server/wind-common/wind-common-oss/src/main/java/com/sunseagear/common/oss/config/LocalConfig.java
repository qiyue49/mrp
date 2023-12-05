package com.sunseagear.common.oss.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.common.oss.config
 * @title:
 * @description: 阿里云配置 * @date: 2018/4/26 9:39
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@Getter
@ConfigurationProperties(prefix = "oss")
public class LocalConfig {
    //外部访问域名
    String domain = "";
    //上传路径
    String uploadFilePath = "";

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }
}
