package com.sunseagear.wind.modules.biz.company.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.biz.company.service.ICompanyService;
import com.sunseagear.wind.modules.biz.company.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.io.IOException;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.company
 * @title: 供货厂家&mdash;&mdash;级联表主表（飞廉演示）控制器
 * @description: 供货厂家&mdash;&mdash;级联表主表（飞廉演示）控制器
 * @author: 
 * @date: 2021-10-21 07:59:27
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("biz/company/company")
@RequiresPermissions("biz:company:company")
@Log(title = "供货厂家&mdash;&mdash;级联表主表（飞廉演示）")
public class CompanyController extends BaseBeanController<Company> {

    @Autowired
    private ICompanyService companyService;
    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @RequiresPermissions("biz:company:company:list")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        // 预处理
        Page pageBean = companyService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @RequiresPermissions("biz:company:company:add")
    public String add(Company entity, BindingResult result,
                      HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        companyService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @RequiresPermissions("biz:company:company:update")
    public String update(Company entity, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        companyService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @RequiresPermissions("biz:company:company:detail")
    public String detail(Model model, @PathVariable("id") String id) {
        Company company = companyService.selectById(id);
        return Response.successJson(company);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @RequiresPermissions("biz:company:company:delete")
    public String batchDelete(@RequestParam("ids") String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        companyService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

}