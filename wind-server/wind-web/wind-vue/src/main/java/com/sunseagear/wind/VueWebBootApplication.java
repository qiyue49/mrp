package com.sunseagear.wind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind
 * @title: Web启动入口
 * @description: Web启动入口 * @date: 2018/5/22 14:56
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@ComponentScan({"com.sunseagear.common.datarule.config", "com.sunseagear.common.tenant", "com.sunseagear.common.quartz.config", "com.sunseagear.common.oss", "com.sunseagear.common.sms", "com.sunseagear.wind"})
@SpringBootApplication
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class VueWebBootApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(VueWebBootApplication.class, args);
    }
}

