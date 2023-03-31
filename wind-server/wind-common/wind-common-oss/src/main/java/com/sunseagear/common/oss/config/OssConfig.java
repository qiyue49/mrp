package com.sunseagear.common.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.common.oss.config
 * @title:
 * @description: OSS配置 * @date: 2018/4/26 9:43
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    //使用的上传存储空间,local本地,aliyun:阿里云,tencent:腾讯云
    String clientType = "";
    //默认上传目录
    String baseDir = "";
    //允许的文件扩展名
    String allowedExtension = "";
    //最大文件大小 50M
    long maxSize = 52428800;
    //是否需要按照日期存放文件
    boolean needDatePath = false;
    boolean originalName = false;

    private AliyunConfig aliyun;

    private LocalConfig local;

    private TencentConfig tencent;

    private QiniuConfig qiniu;
}
