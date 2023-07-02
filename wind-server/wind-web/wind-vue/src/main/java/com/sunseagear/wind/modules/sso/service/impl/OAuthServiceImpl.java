package com.sunseagear.wind.modules.sso.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.utils.CacheUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.common.utils.entity.Principal;
import com.sunseagear.wind.common.helper.JWTHelper;
import com.sunseagear.wind.modules.sso.service.IOAuthService;
import com.sunseagear.wind.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.sso.server.service.impl
 * @title:
 * @description: 单点登录 * @date: 2018/3/29 9:30
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@Service("oAuthService")
public class OAuthServiceImpl implements IOAuthService {


    private final String PREFIX = "";
    // 如果要求服务器重启后所有用户全部重新登录，则采用这种配置。
//    private final String PREFIX = UserUtils.USER_CACHE + ":";

    private final String AUTH_CODE = PREFIX + "auth_code:";
    private final String ACCESS_TOKEN = PREFIX + "access_token:";
    private final String REFRESH_TOKEN = PREFIX + "refresh_token:";

    @Override
    public void addAuthCode(String authCode, Principal principal) {
        CacheUtils.setCacheObject(AUTH_CODE + authCode, principal, getExpireIn(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void addAccessToken(String accessToken, Principal principal) {
        CacheUtils.setCacheObject(ACCESS_TOKEN + accessToken, principal, getExpireIn(), TimeUnit.MILLISECONDS);

    }

    @Override
    public void addRefreshToken(String refreshToken, Principal principal) {
        CacheUtils.setCacheObject(REFRESH_TOKEN + refreshToken, principal, getRefreshExpireIn(), TimeUnit.MILLISECONDS);
    }

    @Override
    public Principal getPrincipalByAuthCode(String authCode) {
        return CacheUtils.getCacheObject(AUTH_CODE + authCode);
    }

    @Override
    public Principal getPrincipalByAccessToken(String accessToken) {
        return CacheUtils.getCacheObject(ACCESS_TOKEN + accessToken);
    }

    @Override
    public Principal getPrincipalByRefreshToken(String refreshToken) {
        return CacheUtils.getCacheObject(REFRESH_TOKEN + refreshToken);
    }

    @Override
    public boolean checkAuthCode(String authCode) {
        try {
            return CacheUtils.getCacheObject(AUTH_CODE + authCode) != null;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean checkAccessToken(String accessToken) {
        try {
            return CacheUtils.getCacheObject(ACCESS_TOKEN + accessToken) != null;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean checkRefreshToken(String refreshToken) {
        try {
            return CacheUtils.getCacheObject(REFRESH_TOKEN + refreshToken) != null;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public void revokeToken(String accessToken) {
        CacheUtils.deleteCacheObject(ACCESS_TOKEN + accessToken);
    }

    @Override
    public boolean checkClientId(String clientId) {
        return Boolean.TRUE;
    }

    @Override
    public boolean checkClientSecret(String clientSecret) {
        return Boolean.TRUE;
    }

    @Override
    public long getExpireIn() {
        return JWTHelper.getExpireIn();
    }

    public static long getRefreshExpireIn() {
        return JWTHelper.getRefreshExpireIn();
    }

    @Override
    public List<Principal> activePrincipal() {
        List<Principal> principalList = new ArrayList<>();
        Set<String> accessTokenList = CacheUtils.keys(ACCESS_TOKEN);
        for (String accessToken : accessTokenList) {
            Principal principal = getPrincipalByAccessToken(accessToken.replace(ACCESS_TOKEN,""));
            if (principal != null) {
                principalList.add(principal);
            }
        }
        return principalList;
    }

    @Override
    public Page<Principal> activePrincipal(Page page, HttpServletRequest request) {
        List<Principal> activePrincipalList = activePrincipal();
        int currentRow = (int) ((page.getCurrent() - 1) * page.getSize());
        List<Principal> dataList = new ArrayList<>();
        String username = request.getParameter("username");
        String loginIp = request.getParameter("loginIp");


        for (int i = currentRow; i < currentRow + page.getSize() && i < activePrincipalList.size(); i++) {
            if (!StringUtils.isEmpty(username)) {
                if (!activePrincipalList.get(i).getUsername().contains(username)) {
                    continue;
                }
            }
            if (!StringUtils.isEmpty(loginIp)) {
                if (!activePrincipalList.get(i).getLoginIp().equals(loginIp)) {
                    continue;
                }
            }
            dataList.add(activePrincipalList.get(i));
        }
        page.setRecords(dataList);
        page.setTotal(activePrincipalList.size());
        return page;
    }
}
