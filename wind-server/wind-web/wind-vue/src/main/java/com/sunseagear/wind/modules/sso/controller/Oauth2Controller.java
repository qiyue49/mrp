package com.sunseagear.wind.modules.sso.controller;

import com.sunseagear.common.datarule.handler.DataRuleHandler;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.utils.ExceptionUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.common.utils.entity.Principal;
import com.sunseagear.wind.common.helper.JWTHelper;
import com.sunseagear.wind.common.response.ResponseError;
import com.sunseagear.wind.modules.sso.service.IOAuthService;
import com.sunseagear.wind.utils.LoginLogUtils;
import com.sunseagear.wind.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    @RequestMapping("/accessToken")
    @ResponseBody
    public String accessToken(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            LoginLogUtils.recordFailLoginLog(UserUtils.getUser().getUsername(), "用户名密码不能为空");
            return Response.failJson("用户名密码不能为空");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
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
            dataRuleHandler.refreshUser(principal.getId().toString());
            LoginLogUtils.recordSuccessLoginLog(UserUtils.getUser().getUsername(), "退出成功");

            return Response.successJson(new Token(accessToken, refreshToken));

        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            LoginLogUtils.recordFailLoginLog(UserUtils.getUser().getUsername(), "找不到用户");
            return Response.failJson("找不到用户");
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            LoginLogUtils.recordFailLoginLog(UserUtils.getUser().getUsername(), "用户名密码错误");
            return Response.failJson("用户名密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            LoginLogUtils.recordFailLoginLog(UserUtils.getUser().getUsername(), ExceptionUtils.getStackTraceAsString(e));
            return Response.failJson("登录失败");
        }
    }


    @RequestMapping("/refreshToken")
    @ResponseBody
    public String refreshToken(HttpServletRequest request) {
        String refreshToken = request.getParameter("refreshToken");

        //生成Access Token
        Principal principal = oAuthService.getPrincipalByRefreshToken(refreshToken);
        if (principal == null) {
            LoginLogUtils.recordFailLoginLog(UserUtils.getUser().getUsername(), "REFRESH_TOKEN过期");
            return Response.error(ResponseError.INVALID_REFRESH_TOKEN, "REFRESH_TOKEN过期");
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", principal.getId());
        dataMap.put("username", principal.getUsername());
        dataMap.put("realname", principal.getRealname());
        dataMap.put("tenantId", principal.getTenantId());
        dataMap.put("roleId", principal.getRoleId());
        final String accessToken = jwtHelper.createToken(dataMap, principal.getUsername());
        oAuthService.addAccessToken(accessToken, principal);

        //生成Refresh Token，只有长时间无任何操作才会过期，过期就立即登出
        final String refreshTokenNew = jwtHelper.createRefreshToken(dataMap, principal.getUsername());
        oAuthService.addRefreshToken(refreshTokenNew, principal);
        LoginLogUtils.recordSuccessLoginLog(UserUtils.getUser().getUsername(), "REFRESH_TOKEN更新");

        return Response.successJson(new Token(accessToken, refreshTokenNew));
    }

    /**
     * 回收TOKEN
     *
     * @param request
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping("/revokeToken")
    @ResponseBody
    public String revokeToken(HttpServletRequest request) {
        String accessToken = request.getHeader("accessToken");
        oAuthService.revokeToken(accessToken);
        LoginLogUtils.recordLogoutLoginLog(UserUtils.getUser().getUsername(), "退出成功");
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
