package com.sunseagear.wind.modules.sso.controller;

import com.sunseagear.common.datarule.handler.DataRuleHandler;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.common.utils.entity.Principal;
import com.sunseagear.wind.common.helper.JWTHelper;
import com.sunseagear.wind.modules.sso.service.IOAuthService;
import com.sunseagear.wind.utils.LoginLogUtils;
import com.sunseagear.wind.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.api.web.controller
 * @title:
 * @description: Oauth2.0认证开发 * @date: 2018/1/8 15:56
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sso/oauth2")
public class Oauth2Controller {

    @Autowired
    private IOAuthService oAuthService;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private DataRuleHandler dataRuleHandler;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登陆方法
     *
     * @param request
     * @return
     */
    @RequestMapping("/access_token")
    @ResponseBody
    public String accessToken(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Response.failJson("用户名密码不能为空");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("用户名密码错误");
        }
        //生成Access Token
        Principal principal = (Principal) authentication.getPrincipal();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", principal.getId());
        dataMap.put("username", principal.getUsername());
        dataMap.put("realname", principal.getRealname());
        dataMap.put("tenantId", principal.getTenantId());
        dataMap.put("roleId", principal.getRoleId());
        final String accessToken = jwtHelper.createToken(dataMap, username);
        oAuthService.addAccessToken(accessToken, principal);

        //生成Refresh Token
        final String refreshToken = jwtHelper.createRefreshToken(dataMap, username);
        oAuthService.addRefreshToken(refreshToken, principal);

        //将用户信息缓存到数据权限模块
        dataRuleHandler.refreshUser(principal.getId());

        return Response.successJson(new Token(accessToken, refreshToken));
    }


    @RequestMapping("/refresh_token")
    @ResponseBody
    public String refreshToken(HttpServletRequest request) {
            String refreshToken = request.getParameter("refresh_token");

            //生成Access Token
            Principal principal = oAuthService.getPrincipalByRefreshToken(refreshToken);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id", principal.getId());
            dataMap.put("username", principal.getUsername());
            dataMap.put("realname", principal.getRealname());
            dataMap.put("tenantId", principal.getTenantId());
            dataMap.put("roleId", principal.getRoleId());
            final String accessToken = jwtHelper.createToken(dataMap, principal.getUsername());
            oAuthService.addAccessToken(accessToken, principal);

            //生成Refresh Token
//            final String refreshToken = jwtHelper.createRefreshToken(dataMap, principal.getUsername());
//            oAuthService.addRefreshToken(refreshToken, principal);

            return Response.successJson(new Token(accessToken, refreshToken));
    }

    /**
     * 回收TOKEN
     *
     * @param request
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping("/revoke_token")
    @ResponseBody
    public String revokeToken(HttpServletRequest request) {
        String accessToken = request.getHeader("access_token");
        LoginLogUtils.recordLogoutLoginLog(UserUtils.getUser().getUsername(), "退出成功");
        oAuthService.revokeToken(accessToken);
        return Response.successJson("退出成功");
    }
}

@Data
class Token {
    String accessToken;
    String refreshToken;

    public Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
