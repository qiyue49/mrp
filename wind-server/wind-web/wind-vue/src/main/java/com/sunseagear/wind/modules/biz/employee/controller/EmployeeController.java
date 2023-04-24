package com.sunseagear.wind.modules.biz.employee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.biz.employee.entity.Employee;
import com.sunseagear.wind.modules.biz.employee.service.IEmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.employee
 * @title: 员工信息&mdash;&mdash;左树右表主表（飞廉演示）控制器
 * @description: 员工信息&mdash;&mdash;左树右表主表（飞廉演示）控制器
 * @author:
 * @date: 2021-05-20 14:04:18
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("biz/employee/employee")
@PreAuthorize("hasAuthority('biz:employee:employee')")
@Log(title = "员工信息&mdash;&mdash;左树右表主表（飞廉演示）")
public class EmployeeController extends BaseBeanController<Employee> {

    @Autowired
    private IEmployeeService employeeService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('biz:employee:employee:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        String sex = request.getParameter("sex");
        if (!StringUtils.isEmpty(sex)) {
            queryWrapper.eq("sex", sex);
        }
        String orgId = request.getParameter("orgId");
        if (!StringUtils.isEmpty(orgId)) {
            queryWrapper.eq("org_id", orgId);
        }
        // 预处理
        Page pageBean = employeeService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('biz:employee:employee:add')")
    public String add(Employee entity, BindingResult result,
                      HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        employeeService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('biz:employee:employee:update')")
    public String update(Employee entity, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        employeeService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('biz:employee:employee:detail')")
    public String detail(Model model, @PathVariable("id") String id) {
        Employee employee = employeeService.selectById(id);
        return Response.successJson(employee);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('biz:employee:employee:delete')")
    public String batchDelete(@RequestParam("ids") String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        employeeService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
