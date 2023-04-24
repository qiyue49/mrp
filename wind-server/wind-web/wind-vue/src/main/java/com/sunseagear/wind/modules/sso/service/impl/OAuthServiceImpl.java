package com.sunseagear.wind.modules.sso.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.utils.CacheUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.common.utils.entity.Principal;
import com.sunseagear.wind.common.helper.JWTHelper;
import com.sunseagear.wind.modules.sso.service.IOAuthService;
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


    private final String ACCESS_TOKEN_KEY = "access_token:key";
    private final String AUTH_CODE_PRE = "auth_code_pre:";
    private final String ACCESS_TOKEN_PRE = "access_token:pre:";
    private final String REFRESH_TOKEN_PRE = "refresh_token_pre:";

    @Override
    public void addAuthCode(String authCode, Principal principal) {
        CacheUtils.setCacheObject(AUTH_CODE_PRE + authCode, principal, getExpireIn(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void addAccessToken(String accessToken, Principal principal) {
        CacheUtils.setCacheObject(ACCESS_TOKEN_PRE + accessToken, principal, getExpireIn(), TimeUnit.MILLISECONDS);
        CacheUtils.addCacheSet(ACCESS_TOKEN_KEY, accessToken);
        CacheUtils.expire(ACCESS_TOKEN_KEY, getExpireIn(), TimeUnit.MILLISECONDS);//设置过期时间

    }

    @Override
    public void addRefreshToken(String refreshToken, Principal principal) {
        CacheUtils.setCacheObject(REFRESH_TOKEN_PRE + refreshToken, principal, getRefreshExpireIn(), TimeUnit.MILLISECONDS);
    }

    @Override
    public Principal getPrincipalByAuthCode(String authCode) {
        return CacheUtils.getCacheObject(AUTH_CODE_PRE + authCode);
    }

    @Override
    public Principal getPrincipalByAccessToken(String accessToken) {
        return CacheUtils.getCacheObject(ACCESS_TOKEN_PRE + accessToken);
    }

    @Override
    public Principal getPrincipalByRefreshToken(String refreshToken) {
        return CacheUtils.getCacheObject(REFRESH_TOKEN_PRE + refreshToken);
    }

    @Override
    public boolean checkAuthCode(String authCode) {
        try {
            return CacheUtils.getCacheObject(AUTH_CODE_PRE + authCode) != null;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean checkAccessToken(String accessToken) {
        try {
            return CacheUtils.getCacheObject(ACCESS_TOKEN_PRE + accessToken) != null;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean checkRefreshToken(String refreshToken) {
        try {
            return CacheUtils.getCacheObject(REFRESH_TOKEN_PRE + refreshToken) != null;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public void revokeToken(String accessToken) {
        CacheUtils.deleteCacheObject(ACCESS_TOKEN_PRE + accessToken);
        CacheUtils.removeCacheSet(ACCESS_TOKEN_KEY, accessToken);
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
    public Integer getExpireIn() {
        return JWTHelper.getExpireIn();
    }

    public static int getRefreshExpireIn() {
        return JWTHelper.getRefreshExpireIn();
    }

    @Override
    public List<Principal> activePrincipal() {
        List<Principal> principalList = new ArrayList<>();
        Set<String> accessTokenList = CacheUtils.getCacheSet(ACCESS_TOKEN_KEY);
        for (String accessToken : accessTokenList) {
            Principal principal = getPrincipalByAccessToken(accessToken);
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
