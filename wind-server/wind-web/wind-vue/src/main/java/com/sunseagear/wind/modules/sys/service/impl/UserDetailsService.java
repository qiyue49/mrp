package com.sunseagear.wind.modules.sys.service.impl;

import com.sunseagear.common.utils.entity.Principal;
import com.sunseagear.wind.modules.sys.entity.User;
import com.sunseagear.wind.modules.sys.service.IUserService;
import com.sunseagear.wind.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private IUserService userService;

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            user = userService.findByEmail(username);
            if (user == null) {
                user = userService.findByPhone(username);
            }
        }

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " does not exist");
        }
        Principal principal = new Principal(user.getId(), user.getUsername(), user.getRealname(), user.getTenantId(), user.findRoleIds());
        principal.setPassword(user.getPassword());
        principal.setPermission(getAuthorities(user));
        return principal;
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        UserUtils.getPermissionSet(user).stream()
                .map(p -> new SimpleGrantedAuthority(p))
                .forEach(authorities::add);

        return authorities;
    }

}
