package com.sunseagear.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.PrintWriter;
import java.util.Objects;


public class ServletUtils {

    /**
     * 获取当前请求对象
     *
     */
    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前请求对象
     *
     */
    public static HttpServletResponse getResponse() {
        try {
            return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否是Ajax
     *
     */
    public static Boolean isAjax() {
        String requestType = Objects.requireNonNull(getRequest()).getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(requestType)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 打印JSON
     *
     */
    public static void printJson(HttpServletResponse response, String content) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(content);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取域名
     *
     */
    public static String getContextUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        return tempContextUrl + "/";
    }

}
