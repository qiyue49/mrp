package com.sunseagear.wind.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.sys.entity.Tenant;
import com.sunseagear.wind.modules.sys.service.ITenantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package sys
 * @title: 租户管理控制器
 * @description: 租户管理控制器
 * @author: 未知
 * @date: 2019-11-28 06:24:52
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("sys/tenant")
@PreAuthorize("hasAuthority('sys:tenant')")
@Log(title = "租户管理")
public class TenantController extends BaseBeanController<Tenant> {


    @Autowired
    private ITenantService tenantService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:tenant:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("a.create_date");
        String tenantId = request.getParameter("tenantId");
        if (!StringUtils.isEmpty(tenantId)) {
            queryWrapper.eq("tenant_id", tenantId);
        }
        String contact = request.getParameter("contact");
        if (!StringUtils.isEmpty(contact)) {
            queryWrapper.eq("contact", contact);
        }
        String phone = request.getParameter("phone");
        if (!StringUtils.isEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        // 预处理
        Page pageBean = tenantService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean, "remarks", false);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('sys:tenant:add')")
    public String add(@Valid Tenant entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        entity.setTenantId(StringUtils.randomNumber(8));
        tenantService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('sys:tenant:update')")
    public String update(@Valid Tenant entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        tenantService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:tenant:detail')")
    public String detail(@PathVariable("id") Long id) {
        Tenant tenant = tenantService.selectById(id);
        return Response.successJson(tenant);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('sys:tenant:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        tenantService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
