package com.sunseagear.wind.modules.test.expandtable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.test.expandtable.entity.ExpandTable;
import com.sunseagear.wind.modules.test.expandtable.service.IExpandTableService;
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
 * @package test.expandtable
 * @title: 商品信息控制器
 * @description: 商品信息控制器
 * @author: admin
 * @date: 2019-11-13 15:02:00
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("test/expandtable/expandtable")
@PreAuthorize("hasAuthority('test:expandtable:expandtable')")
@Log(title = "商品信息")
public class ExpandTableController extends BaseBeanController<ExpandTable> {

    @Autowired
    private IExpandTableService expandTableService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:expandtable:expandtable:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<ExpandTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        // 预处理
        Page pageBean = expandTableService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('test:expandtable:expandtable:add')")
    public String add(@Valid ExpandTable entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        expandTableService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('test:expandtable:expandtable:update')")
    public String update(@Valid ExpandTable entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        expandTableService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:expandtable:expandtable:detail')")
    public String detail(@PathVariable("id") Long id) {
        ExpandTable expandTable = expandTableService.selectById(id);
        return Response.successJson(expandTable);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('test:expandtable:expandtable:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        expandTableService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
