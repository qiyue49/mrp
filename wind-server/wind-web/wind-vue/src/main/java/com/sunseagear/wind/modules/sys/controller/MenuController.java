package com.sunseagear.wind.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.common.helper.MenuTreeHelper;
import com.sunseagear.wind.common.helper.VueTreeHelper;
import com.sunseagear.wind.modules.sys.entity.Menu;
import com.sunseagear.wind.modules.sys.service.IMenuService;
import com.sunseagear.wind.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/sys/menu")
@PreAuthorize("hasAuthority('sys:menu')")
@Log(title = "菜单管理")
public class MenuController extends BaseBeanController<Menu> {

    @Autowired
    private IMenuService menuService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public String list(HttpServletRequest request) throws IOException {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        //加入条件
        String keyword = request.getParameter("keyword");
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("name", keyword);
        }
        queryWrapper.orderByAsc("sort");
        List<Menu> treeNodeList = menuService.selectList(queryWrapper);
        List<Menu> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList);
        return Response.successJson(vueTreeNodes);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('sys:menu:add')")
    public String add(@Valid Menu entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        menuService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public String update(@Valid Menu entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        menuService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:menu:detail')")
    public String detail(@PathVariable("id") Long id) {
        Menu organization = menuService.selectById(id);
        return Response.successJson(organization);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = Arrays.asList(ids);
        menuService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    /**
     * 获得菜单列表
     *
     * @throws IOException
     */
    @GetMapping(value = "getMenus")
    public String getMenus() {
        List<Menu> menuList = menuService.getCurrentUserMenus();
        List<MenuTreeHelper.MenuTreeNode> menuTreeNodes = MenuTreeHelper.create().sort(menuList);
        return Response.successJson(menuTreeNodes);
    }

    @PostMapping("{id}/changeSort")
    public String changeSort(@PathVariable("id") Long id,
                             @RequestParam("sort") Integer sort) {
        menuService.changeSort(id, sort);
        return Response.ok("排序成功");
    }

    /**
     * 获得菜单列表
     *
     * @throws IOException
     */
    @GetMapping(value = "getPermissions")
    public String getPermissions() {
        //加入条件
        Long uid = UserUtils.getUser().getId();
        List<Menu> permissionValueList = menuService.findPermissionByUserId(uid);
        List<String> menuIdList = new ArrayList<>();
        for (Menu menu : permissionValueList) {
            menuIdList.add(menu.getPermission());
        }
        return Response.successJson(menuIdList);
    }

    @PostMapping("{id}/generate/button")
    @Log(logType = LogType.OTHER, title = "生成按钮")
    @PreAuthorize("hasAuthority('sys:menu:generate:button')")
    public String generateButton(@PathVariable("id") Long id,
                                 @RequestParam("parentPermission") String parentPermission,
                                 @RequestParam("permissions") String permissions,
                                 @RequestParam("permissionTitles") String permissionTitles,
                                 @RequestParam("additional") Boolean additional) {
        menuService.generateButton(id, parentPermission, permissions.split(","), permissionTitles.split(","), additional);
        return Response.ok("生成成功");
    }
}
