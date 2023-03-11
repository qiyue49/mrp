package com.sunseagear.wind.modules.biz.department.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.access.prepost.PreAuthorize;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.common.helper.VueTreeHelper;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.biz.department.service.IDepartmentService;
import com.sunseagear.wind.modules.biz.department.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.io.IOException;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.department
 * @title: 组织架构&mdash;&mdash;左树右表树表（飞廉演示）控制器
 * @description: 组织架构&mdash;&mdash;左树右表树表（飞廉演示）控制器
 * @author:
 * @date: 2021-04-15 10:23:14
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("biz/department/department")
@PreAuthorize("hasAuthority('biz:department:department')")
@Log(title = "组织架构&mdash;&mdash;左树右表树表（飞廉演示）")
public class DepartmentController extends BaseBeanController<Department> {

    @Autowired
    private IDepartmentService departmentService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('biz:department:department:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        // 预处理
        List<Department> treeNodeList = departmentService.selectTreeList(queryWrapper);
        List<Department> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList);
        return Response.successJson(vueTreeNodes, "address", false);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('biz:department:department:add')")
    public String add(Department entity, BindingResult result,
                      HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        departmentService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('biz:department:department:update')")
    public String update(Department entity, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        departmentService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('biz:department:department:detail')")
    public String detail(Model model, @PathVariable("id") String id) {
        Department department = departmentService.selectById(id);
        return Response.successJson(department);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('biz:department:department:delete')")
    public String batchDelete(@RequestParam("ids") String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        departmentService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
