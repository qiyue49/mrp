package com.sunseagear.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.Headers;

import java.util.HashMap;
import java.util.Map;

public class IpUtils {
    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getRealAddressByIP(String ip) {
        String address = "";
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("ip", ip);
            Headers headers = new Headers.Builder().add("accept", "*/*").
                    add("connection", "Keep-Alive").
                    add("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)").build();
            String rspStr = HttpUtils.SyncGet(IP_URL + "?ip=" + ip, headers);
            if (StringUtils.isEmpty(rspStr)) {
                return address;
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(rspStr, JsonObject.class);
            String region = jsonObject.get("region").getAsString();
            String city = jsonObject.get("city").getAsString();
            address = region + " " + city;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

}
