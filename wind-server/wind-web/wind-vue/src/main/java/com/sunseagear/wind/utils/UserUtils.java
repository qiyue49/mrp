package com.sunseagear.wind.utils;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.sunseagear.common.utils.CacheUtils;
import com.sunseagear.common.utils.ObjectUtils;
import com.sunseagear.common.utils.SpringContextHolder;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.common.utils.entity.Principal;
import com.sunseagear.wind.modules.sys.entity.Menu;
import com.sunseagear.wind.modules.sys.entity.Role;
import com.sunseagear.wind.modules.sys.entity.User;
import com.sunseagear.wind.modules.sys.service.IMenuService;
import com.sunseagear.wind.modules.sys.service.IRoleService;
import com.sunseagear.wind.modules.sys.service.IUserService;
import com.sunseagear.wind.modules.sys.service.impl.MenuServiceImpl;
import com.sunseagear.wind.modules.sys.service.impl.RoleServiceImpl;
import com.sunseagear.wind.modules.sys.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @title: UserUtils.java
 * @package com.sunseagear.wind.modules.sys.utils
 * @description: 用户工具类 * @date: 2017年6月26日 下午6:00:39
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@SuppressWarnings("unchecked")
public class UserUtils extends com.sunseagear.common.utils.UserUtils {

    private static IUserService userService = SpringContextHolder.getBean(UserServiceImpl.class);
    private static IRoleService roleService = SpringContextHolder.getBean(RoleServiceImpl.class);
    private static IMenuService menuService = SpringContextHolder.getBean(MenuServiceImpl.class);
    public static final String USER_CACHE = "sys_user";
    public static final String USER_CACHE_ID = "sys_user:id:";
    public static final String USER_CACHE_USER_NAME = "sys_user:username:";
    public static final String CACHE_ROLE_LIST = "sys_user:role:";
    public static final String CACHE_PERMISSION_LIST = "sys_user:permission:";

    /**
     * 获取当前用户
     *
     * @return 取不到返回 new User()
     */
    public static User getUser() {
        Principal principal = getPrincipal();
        if (principal != null) {
            User user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return new User();
        }
        // 如果没有登录，则返回实例化空的User对象。
        User user = new User();
        return user;
    }

    /**
     * 根据ID获取用户
     *
     * @param id
     * @return 取不到返回null
     */
    public static User get(Long id) {
        User user = CacheUtils.getCacheObject(USER_CACHE_ID + id);
        if (user == null) {
            user = userService.selectById(id);
            if (user == null) {
                return null;
            }
            CacheUtils.setCacheObject(USER_CACHE_ID + user.getId(), user);
            CacheUtils.setCacheObject(USER_CACHE_USER_NAME + user.getUsername(), user);
        }
        return user;
    }

    /**
     * 根据用户名获取用户
     *
     * @param username
     * @return
     */
    public static User getByUserName(String username) {
        User user = CacheUtils.getCacheObject(USER_CACHE_USER_NAME + username);
        if (user == null) {
            user = userService.findByUsername(username);
            if (user == null) {
                return null;
            }
            CacheUtils.setCacheObject(USER_CACHE_ID + user.getId(), user);
            CacheUtils.setCacheObject(USER_CACHE_USER_NAME + user.getUsername(), user);
        }
        return user;
    }

    /**
     * 获取当前用户角色列表
     *
     * @return
     */
    public static List<Role> getRoleList() {
        User user = getUser();
        return getRoleList(user);
    }

    public static List<Role> getRoleList(User user) {
        List<Role> roleList = CacheUtils.getCacheObject(CACHE_ROLE_LIST + user.getId());
        if (ObjectUtils.isNullOrEmpty(roleList)) {
            roleList = roleService.findListByUserId(user.getId());
            CacheUtils.setCacheObject(CACHE_ROLE_LIST + user.getId(), roleList);
        }
        return roleList;
    }

    /**
     * 获取角色列表
     *
     * @return
     */
    public static Set<String> getRoleStringList() {
        Set<Role> roles = Sets.newConcurrentHashSet(getRoleList());
        return Sets.newHashSet(Collections2.transform(roles, role -> role.getCode()));
    }

    /**
     * 是否有特定的角色
     *
     * @param roleCode
     * @return
     */
    public static boolean hasRole(String roleCode) {
        Set<String> roleCodeList = getRoleStringList();
        for (String roleCodeItem : roleCodeList) {
            if (roleCodeItem.equals(roleCode)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 获取权限列表
     *
     * @return
     */
    public static Set<String> getPermissionSet() {
        User user = getUser();
        return getPermissionSet(user);
    }

    public static Set<String> getPermissionSet(User user) {
        List<String> permissionList = UserUtils.getPermissionList(user);
        Set<String> permissionsList = Sets.newConcurrentHashSet();
        for (String permission : permissionList) {
            if (StringUtils.isNotBlank(permission)) {
                // 添加基于Permission的权限信息
                for (String permissionItem : StringUtils.split(permission, ",")) {
                    if (StringUtils.isNotBlank(permissionItem)) {
                        permissionsList.add(permissionItem);
                    }
                }
            }
        }

        //如果该用户已经分配了权限，则为该用户添加以下权限，这些权限在用于前端sysUser，sysOrganization组件和用户修改自己的密码和个人信息
        if (!permissionsList.isEmpty()) {
            permissionsList.add("sys:user"); // 更新用户信息
            permissionsList.add("sys:user:list"); // sysUser组件
            permissionsList.add("sys:organization:list"); // sysOrganization
            permissionsList.add("sys:dict"); //字典
            permissionsList.add("sys:menu"); //菜单
            permissionsList.add("oss:attachment"); //文件上传
        }
        return permissionsList;
    }

    /**
     * 获取当前用户授权菜单
     *
     * @return
     */
    public static List<String> getPermissionList() {
        User user = getUser();
        return getPermissionList(user);
    }

    public static List<String> getPermissionList(User user) {
        final List<String> permissionList = new ArrayList<>();
        getRoleList(user).forEach(item -> {
            ArrayList<String> cachePermissions = CacheUtils.getCacheObject(CACHE_PERMISSION_LIST + item.getId());
            permissionList.addAll(ObjectUtils.isNullOrEmpty(cachePermissions) ? new ArrayList<>() : cachePermissions);
            if (ObjectUtils.isNullOrEmpty(cachePermissions)) {
                List<Menu> permissionValueList = menuService.findPermissionByRoleId(item.getId());
                List<String> menuIdList = new ArrayList<>();
                for (Menu menu : permissionValueList) {
                    menuIdList.add(menu.getPermission());
                }
                permissionList.addAll(menuIdList);
                // 不加入缓存
                CacheUtils.setCacheObject(CACHE_PERMISSION_LIST + item.getId(), permissionList);
            }
        });
        return permissionList;
    }

    /**
     * 判断是否为管理员
     * 包含（总）系统管理员
     * <p>
     * 管理员拥有全部权限
     */
    public static boolean isAdmin() {
        List<Role> roleList = getRoleList();
        Boolean bool = false;
        for (Role r : roleList) {
            if (Integer.parseInt(r.getIsSys()) == 1
                    || r.getCode().toLowerCase().contains("admin")
                    || r.getName().contains("系统管理员")) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    /**
     * 更新用户
     *
     * @param id
     */
    public static void update(Long id) {
        User user = userService.selectById(id);
        if (user == null) {
            return;
        }
        clearCache(user);
        CacheUtils.setCacheObject(USER_CACHE_ID + user.getId(), user);
        CacheUtils.setCacheObject(USER_CACHE_USER_NAME + user.getUsername(), user);
        getRoleList();
    }

    /**
     * 清除当前用户缓存
     */
    public static void clearCurrentUserCache() {
        UserUtils.clearCache(getUser());
    }

    /**
     * 清除指定用户缓存
     *
     * @param user
     */
    public static void clearCache(User user) {
        CacheUtils.clear(USER_CACHE_ID + user.getId());
        CacheUtils.clear(USER_CACHE_USER_NAME + user.getUsername());
        CacheUtils.clear(CACHE_ROLE_LIST + user.getId());
    }

    /**
     * 清除权限缓存
     *
     * @param roleId
     */
    public static void clearPermissionCache(Long roleId) {
        CacheUtils.clear(CACHE_PERMISSION_LIST + roleId);
    }

    /**
     * 清除所有用户的缓存
     */
    public static void clearAllUserCache() {
        CacheUtils.clear(USER_CACHE);
    }
}
