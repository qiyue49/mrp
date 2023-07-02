package com.sunseagear.wind.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.tenant.TenantProperties;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.common.helper.VueTreeHelper;
import com.sunseagear.wind.common.response.ResponseError;
import com.sunseagear.wind.modules.sys.entity.Menu;
import com.sunseagear.wind.modules.sys.entity.Role;
import com.sunseagear.wind.modules.sys.service.IMenuService;
import com.sunseagear.wind.modules.sys.service.IRoleMenuService;
import com.sunseagear.wind.modules.sys.service.IRoleService;
import com.sunseagear.wind.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.sys.controller
 * @title: 消息模版控制器
 * @description: 消息模版控制器 * @date: 2018-09-03 15:10:10
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/sys/role")
@Log(title = "角色管理")
public class RoleController extends BaseBeanController<Role> {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IRoleMenuService roleMenuService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (UserUtils.getUser().getTenantId().equals(TenantProperties.getInstance().getDefaultTenantId())) {
            queryWrapper.eq("tenant_id", UserUtils.getTenantId());
        } else {
            queryWrapper.nested(i -> i.eq("tenant_id", UserUtils.getTenantId()).or().eq("tenant_id", TenantProperties.getInstance().getDefaultTenantId())).eq("is_sys", "0");
        }
        queryWrapper.orderByDesc("create_date");
        String code = request.getParameter("code");
        if (!StringUtils.isEmpty(code)) {
            queryWrapper.like("code", code);
        }
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        // 预处理
        Page pageBean = roleService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    /**
     * 获取可用的用户列表
     *
     * @throws IOException
     */
    @GetMapping(value = "usable/list")
    public String usableLst() throws IOException {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        if (UserUtils.getUser().getTenantId().equals(TenantProperties.getInstance().getDefaultTenantId())) {
            queryWrapper.eq("tenant_id", UserUtils.getTenantId());
        } else {
            queryWrapper.nested(i -> i.eq("tenant_id", UserUtils.getTenantId()).or().eq("tenant_id", TenantProperties.getInstance().getDefaultTenantId())).eq("is_sys", "0");
        }
        queryWrapper.eq("usable", "1");
        queryWrapper.orderByDesc("create_date");
        List<Role> usableLst = roleService.selectList(queryWrapper);
        return Response.successJson(usableLst);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    public String add(@Valid Role entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        entity.setTenantId(UserUtils.getTenantId());
        roleService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    public String update(@Valid Role entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        roleService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        roleService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    /**
     * 通过用户ID获得角色
     *
     * @param uid
     * @return
     */
    @PostMapping(value = "{uid}/findListByUserId")
    public List<Role> findListByUserId(@PathVariable("uid") Long uid) {
        try {
            return roleService.findListByUserId(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping(value = "{roleId}/menu")
    public String menu(@PathVariable("roleId") Long roleId) {
        Role role = roleService.getById(roleId);
        Map<String, Object> dataMap = new HashMap<>();
        List<Menu> treeNodeList;
        if (role.getCode().equals("admin")) {
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
            queryWrapper.orderByAsc("sort").ne("type", Menu.BUTTON);
            treeNodeList = menuService.selectList(queryWrapper);
        } else {
            treeNodeList = menuService.getCurrentUserMenus();
        }
        List<Menu> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList);
        dataMap.put("menus", vueTreeNodes);
        // 获得选择的
        List<Menu> menuList = menuService.findMenuByRoleId(roleId);
        List<Long> menuIdList = new ArrayList<>();
        for (Menu menu : menuList) {
            menuIdList.add(menu.getId());
        }
        dataMap.put("selectMenuIds", menuIdList);
        return Response.successJson(dataMap);
    }

    @GetMapping(value = "{roleId}/permission")
    public String permission(@PathVariable("roleId") Long roleId) {
        Role role = roleService.getById(roleId);
        Map<String, Object> dataMap = new HashMap<>();
        List<Menu> treeNodeList;
        if (role.getCode().equals("admin")) {
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("sort");
            treeNodeList = menuService.selectList(queryWrapper);
        } else {
            treeNodeList = menuService.findMenuAndPermissionByUserId(UserUtils.getUser().getId());
        }
        List<Menu> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList);
        dataMap.put("menus", vueTreeNodes);
        // 获得选择的
        List<Menu> menuList = menuService.findPermissionByRoleId(roleId);
        List<Long> menuIdList = new ArrayList<>();
        for (Menu menu : menuList) {
            menuIdList.add(menu.getId());
        }
        dataMap.put("selectMenuIds", menuIdList);
        return Response.successJson(dataMap);
    }

    @PostMapping(value = "/setMenu")
    @Log(logType = LogType.OTHER, title = "菜单配置")
    public String setMenu(@RequestParam("roleId") Long roleId,
                          @RequestParam("menuIds") String menuIds) {
        try {
            // 权限设置
            roleMenuService.setMenu(roleId, menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResponseError.NORMAL_ERROR, "保存失败!<br />原因:" + e.getMessage());
        }
        return Response.ok("保存成功");
    }

    @PostMapping(value = "/setPermission")
    @Log(logType = LogType.OTHER, title = "权限配置")
    public String setPermission(@RequestParam("roleId") Long roleId,
                                @RequestParam("menuIds") String menuIds) {
        try {
            // 权限设置
            roleMenuService.setPermission(roleId, menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResponseError.NORMAL_ERROR, "保存失败!<br />原因:" + e.getMessage());
        }
        return Response.ok("保存成功");
    }
}
