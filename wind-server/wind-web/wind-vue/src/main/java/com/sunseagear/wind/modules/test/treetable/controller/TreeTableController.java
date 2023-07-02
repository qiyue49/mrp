package com.sunseagear.wind.modules.test.treetable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.common.helper.VueTreeHelper;
import com.sunseagear.wind.modules.test.treetable.entity.TreeTable;
import com.sunseagear.wind.modules.test.treetable.service.ITreeTableService;
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
 * @package test.treetable
 * @title: 树形结构表控制器
 * @description: 树形结构表控制器
 * @author: admin
 * @date: 2019-11-13 21:38:32
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("test/treetable/treetable")
@PreAuthorize("hasAuthority('test:treetable:treetable')")
@Log(title = "树形结构表")
public class TreeTableController extends BaseBeanController<TreeTable> {

    @Autowired
    private ITreeTableService treeTableService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:treetable:treetable:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<TreeTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }

        // 预处理
        List<TreeTable> treeNodeList = treeTableService.selectList(queryWrapper);
        List<TreeTable> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList);
        return Response.successJson(vueTreeNodes);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('test:treetable:treetable:add')")
    public String add(@Valid TreeTable entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        treeTableService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('test:treetable:treetable:update')")
    public String update(@Valid TreeTable entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        treeTableService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('test:treetable:treetable:detail')")
    public String detail(@PathVariable("id") Long id) {
        TreeTable treeTable = treeTableService.selectById(id);
        return Response.successJson(treeTable);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('test:treetable:treetable:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        treeTableService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
