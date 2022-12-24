package com.sunseagear.wind.config.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.config.autoconfigure
 * @title:
 * @description: shiro属性配置文件 * @date: 2018/3/6 10:33
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@Data
@ConfigurationProperties(prefix = "shiro")
public class ShiroConfigProperties {
    //系统管理员密码重试次数
    private Integer userPasswordShowCaptchaRetryCount = 3;
    //凭证匹配器-算法
    private String credentialsHashAlgorithmName = "md5";
    //生成Hash值的迭代次数
    private Integer credentialsHashIterations = 2;

    //表示是否存储散列后的密码为16进制，需要和生成密码时的一样
    private Boolean credentialsStoredCredentialsHexEncoded = Boolean.TRUE;
    //全局session超时时间 1000*30*60milliseconds = 30 分钟（1800000）
    private Integer sessionGlobalSessionTimeout = 1800000;
    //session验证时间间隔(即验证会话是否还有效) 1000*60*60milliseconds = 1小时（3600000）
    private Integer sessionValidationInterval = 3600000;
    //session 缓存的名字
    private String activeSessionCacheName = "shiro-activeSessionCache";
    //CookieName
    private String sessionIdCookieName = "shiro-sessionId";
    //CookieName
    private String sessionIdCookieDomain = "";
    private String sessionIdCookiePath = "/";
    private Boolean sessionIdCookieHttpOnly = Boolean.TRUE;
    //默认uid cookie 浏览器关闭后销毁
    private Integer sessionIdCookieMaxAge = -1;
    //rememeber me cookie 名字
    private String rememeberMeCookieName = "shiro-rememeberMeCache";
    private String rememeberMeCookieDomain = "";
    private String rememeberMeCookiePath = "/";
    private Boolean rememeberMeCookieHttpOnly = Boolean.TRUE;
    //默认 rememberMe cookie 60 * 60 * 24 * 30 （30天）
    private Integer rememeberMeCookieMaxAge = 2592000;
    //cipherKey
    private String rememeberMeCookieBase64CipherKey = "KU471rVNQ6k7PQL4SqxgJg==";
    //权限
    private String filterChainDefinitions = "";
    //
    private Integer codeExpiresIn = 10 * 60;
    //access_token过期时间，为两小时
    private Integer accesTokenExpiresIn = 2 * 60 * 60;
    //refresh_token过期时间，为30天
    private Integer refreshTokenExpiresIn = 30 * 24 * 60 * 60;
    //TOKEN_JWT_SECRET
    private String jwtTokenSecret = "574d6fc2f8ec8a82f61350868752361d";

}
