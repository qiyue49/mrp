package com.sunseagear.common.utils.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sunseagear.common.utils.IpUtils;
import com.sunseagear.common.utils.ServletUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class Principal implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private Long id; // 编号
    private String username; // 登录名
    private String password; // 密码
    private String realname; // 姓名
    private String tenantId; //租户ID
    private String roleId; //角色ID
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date stopTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastAccessTime;

    private List<GrantedAuthority> permission;
    /**
     * 登录IP
     */
    private String loginIp;
    /**
     * 登录地点
     */
    private String loginLocation;
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * 操作系统
     */
    private String os;

    public Principal() {
    }

    public Principal(Long id, String username, String realname, String tenantId, String roleId) {
        this.id = id;
        this.username = username;
        this.realname = realname;
        this.tenantId = tenantId;
        this.roleId = roleId;
        init();

    }

    private void init() {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        this.startTime = new Date();
        this.browser = browser;
        this.os = os;
        this.loginIp = ip;
    }


    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permission;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
