package com.sunseagear.wind.modules.biz.company.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.biz.company.service.IProductService;
import com.sunseagear.wind.modules.biz.company.entity.Product;
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
 * @title: 产品管理&mdash;&mdash;级联表子表（飞廉演示）控制器
 * @description: 产品管理&mdash;&mdash;级联表子表（飞廉演示）控制器
 * @author: 
 * @date: 2021-10-21 07:59:27
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("biz/company/product")
@RequiresPermissions("biz:company:company")
@Log(title = "产品管理&mdash;&mdash;级联表子表（飞廉演示）")
public class ProductController extends BaseBeanController<Product> {

    @Autowired
    private IProductService productService;
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
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String companyId = request.getParameter("companyId");
        if (!StringUtils.isEmpty(companyId)) {
            queryWrapper.eq("company_id", companyId);
        }
        // 预处理
        Page pageBean = productService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @RequiresPermissions("biz:company:company:add")
    public String add(Product entity, BindingResult result,
                      HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        productService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @RequiresPermissions("biz:company:company:update")
    public String update(Product entity, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        productService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @RequiresPermissions("biz:company:company:detail")
    public String detail(Model model, @PathVariable("id") String id) {
        Product product = productService.selectById(id);
        return Response.successJson(product);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @RequiresPermissions("biz:company:company:delete")
    public String batchDelete(@RequestParam("ids") String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        productService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

}