package com.sunseagear.common.utils.entity;

import com.sunseagear.common.utils.IpUtils;
import com.sunseagear.common.utils.ServletUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class Principal implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id; // 编号
    private String username; // 登录名
    private String password; // 密码
    private String realname; // 姓名
    private String tenantId; //租户ID
    private String roleId; //角色ID
    private Date startTime;
    private Date stopTime;
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
        this.loginIp = IpUtils.getIpAddr(ServletUtils.getRequest());
        // 获取客户端操作系统
        this.os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        this.browser = userAgent.getBrowser().getName();
        this.startTime = new Date();
    }


    @Override
    public String toString() {
        return id.toString();
    }

    @Override
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
