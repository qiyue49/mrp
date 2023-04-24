package com.sunseagear.common.utils;

import com.sunseagear.common.utils.entity.Principal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @title: UserUtils.java
 * @package com.sunseagear.wind.modules.sys.utils
 * @description: 用户工具类 * @date: 2017年6月26日 下午6:00:39
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@SuppressWarnings("unchecked")
public class UserUtils {


    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前登录者对象
     */
    public static Principal getPrincipal() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        if (authentication == null) {
            return null;
        }
        return (Principal) getAuthentication().getPrincipal();
    }

    public static String getTenantId() {
        Principal principal = getPrincipal();
        if (principal == null) {
            return null;
        }
        return principal.getTenantId();
    }


}
