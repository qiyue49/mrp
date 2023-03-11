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
//        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
//        final String refreshToken = oauthIssuerImpl.refreshToken();
//        oAuthService.addRefreshToken(refreshToken, principal);

        //将用户信息缓存到数据权限模块
        dataRuleHandler.refreshUser(principal.getId());


        return Response.successJson((Object) accessToken);
    }


    @RequestMapping("/refresh_token")
    @ResponseBody
    public HttpEntity refreshToken(HttpServletRequest request) {
/*
        try {
            //构建OAuth请求
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

            //检查提交的客户端id是否正确
            if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
                return ResponseUtils.getErrResponse(HttpServletResponse.SC_BAD_REQUEST, ResponseError.INVALID_CLIENT);
            }

            String refreshToken = oauthRequest.getParam(OAuth.OAUTH_REFRESH_TOKEN);
            // 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
                if (!oAuthService.checkRefreshToken(refreshToken)) {
                    return ResponseUtils.getErrResponse(HttpServletResponse.SC_UNAUTHORIZED, ResponseError.INVALID_REFRESH_TOKEN);
                }
            }

            //生成Access Token
            Principal principal = oAuthService.getPrincipalByRefreshToken(refreshToken);
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("id", principal.getId());
            dataMap.put("username", principal.getUsername());
            dataMap.put("realname", principal.getRealname());
            dataMap.put("tenantId", principal.getTenantId());
            dataMap.put("roleId", principal.getRoleId());
            final String accessToken = JWTHelper.sign(dataMap, shiroConfigProperties.getJwtTokenSecret(), Long.parseLong(oAuthService.getExpireIn() + ""));
            oAuthService.addAccessToken(accessToken, oAuthService.getPrincipalByRefreshToken(refreshToken));

            //生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .setExpiresIn(String.valueOf(oAuthService.getExpireIn()))
                    .buildJSONMessage();

            //根据OAuthResponse生成ResponseEntity
            return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthProblemException e) {
            //构建错误响应
            return ResponseUtils.getErrResponse(HttpServletResponse.SC_BAD_REQUEST, ResponseError.INVALID_REFRESH_TOKEN, e.getDescription());
        }*/
        return null;
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
