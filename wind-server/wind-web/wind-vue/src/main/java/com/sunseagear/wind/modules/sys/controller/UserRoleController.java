package com.sunseagear.wind.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.sys.entity.UserRole;
import com.sunseagear.wind.modules.sys.service.IUserRoleService;
import com.sunseagear.wind.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.upms.controller
 * @title: 用户角色控制器
 * @description: 用户角色控制器 * @date: 2018-05-06 18:46:48
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/user/role")
@PreAuthorize("hasAuthority('sys:user:role')")
@Log(title = "用户管理")
public class UserRoleController extends BaseBeanController<UserRole> {

    @Autowired
    private IUserRoleService userRoleService;


    @GetMapping(value = "{userId}/roleIds")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:user:role:list')")
    public String userRoleIds(@PathVariable("userId") Long userId) {
        List<Long> roleIdList = new ArrayList<>();
        List<UserRole> userRoleList = userRoleService.selectList(new QueryWrapper<UserRole>().eq("user_id", userId));
        for (UserRole userRole : userRoleList) {
            roleIdList.add(userRole.getRoleId());
        }
        return Response.successJson(roleIdList);
    }

    /**
     * 新增关系
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @PostMapping("{userId}/insertByUserId")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('sys:role:add')")
    public String insertByUserId(@PathVariable("userId") Long userId, @RequestParam("roleIds") Long[] roleIds) {
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleService.insertByRoleId(userId, roleId);
        }
        UserUtils.clearCache(UserUtils.get(userId));
        return Response.ok("添加成功");
    }

    /**
     * 删除关系
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @PostMapping("{userId}/deleteByUserId")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public String deleteByUserId(@PathVariable("userId") Long userId, @RequestParam("roleIds") String roleIds) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.in("role_id", roleIds);
        userRoleService.delete(queryWrapper);
        UserUtils.clearCache(UserUtils.get(userId));
        return Response.ok("删除成功");
    }

}
