package com.sunseagear.wind.modules.test.treeandtable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.test.treeandtable.entity.TreeAndTable;
import com.sunseagear.wind.modules.test.treeandtable.service.ITreeAndTableService;
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
 * @package test.treeandtable
 * @title: 左树右表控制器
 * @description: 左树右表控制器
 * @author: admin
 * @date: 2019-11-13 21:24:49
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("test/treeandtable/treeandtable")
@PreAuthorize("hasAuthority('test:treeandtable:treeandtable')")
@Log(title = "左树右表")
public class TreeAndTableController extends BaseBeanController<TreeAndTable> {

    @Autowired
    private ITreeAndTableService treeAndTableService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:treeandtable:treeandtable:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<TreeAndTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        String type = request.getParameter("type");
        if (!StringUtils.isEmpty(type)) {
            queryWrapper.eq("type", type);
        }
        String areaId = request.getParameter("areaId");
        if (!StringUtils.isEmpty(areaId)) {
            queryWrapper.eq("area_id", areaId);
        }
        // 预处理
        Page pageBean = treeAndTableService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('test:treeandtable:treeandtable:add')")
    public String add(@Valid TreeAndTable entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        treeAndTableService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('test:treeandtable:treeandtable:update')")
    public String update(@Valid TreeAndTable entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        treeAndTableService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:treeandtable:treeandtable:detail')")
    public String detail(@PathVariable("id") Long id) {
        TreeAndTable treeAndTable = treeAndTableService.selectById(id);
        return Response.successJson(treeAndTable);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('test:treeandtable:treeandtable:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        treeAndTableService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
