package com.sunseagear.wind.security;

import com.sunseagear.common.http.Response;
import com.sunseagear.common.utils.ServletUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.common.utils.UserUtils;
import com.sunseagear.wind.common.helper.JWTHelper;
import com.sunseagear.wind.common.response.ResponseError;
import com.sunseagear.wind.modules.sso.service.IOAuthService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Pattern DO_FILTER_INTERNAL_PATTERN = Pattern.compile("/sso/oauth2/\\**");


    @Autowired
    private JWTHelper jwtService;

    @Autowired
    private IOAuthService oAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        log.info("url======{} token=={}",request.getRequestURI(),token);
        if (!StringUtils.isEmpty(token)) {
            //检索匹配器对象
            Matcher matcher = DO_FILTER_INTERNAL_PATTERN.matcher(request.getRequestURI());
            if (!matcher.find()) {
                try {
                    jwtService.isTokenExpired(token);
                } catch (ExpiredJwtException e) {
                    e.printStackTrace();
                    ServletUtils.printJson(response, Response.error(ResponseError.EXPIRED_ACCESS_TOKEN, "TOKEN过期"));
                    return;
                }

                if (UserUtils.getPrincipal() == null) {
                    UserDetails userDetails = oAuthService.getPrincipalByAccessToken(token);
                    if (userDetails != null && jwtService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
