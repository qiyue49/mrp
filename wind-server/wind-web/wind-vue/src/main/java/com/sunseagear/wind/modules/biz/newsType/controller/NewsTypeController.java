package com.sunseagear.wind.modules.biz.newsType.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.biz.newsType.entity.NewsType;
import com.sunseagear.wind.modules.biz.newsType.service.INewsTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.newsType
 * @title: 新闻类型控制器
 * @description: 新闻类型控制器
 * @author:
 * @date: 2020-11-09 04:00:26
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("biz/newsType/newstype")
@RequiresPermissions("biz:newsType:newstype")
@Log(title = "新闻类型")
public class NewsTypeController extends BaseBeanController<NewsType> {

    @Autowired
    private INewsTypeService newsTypeService;
    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @RequiresPermissions("biz:newsType:newstype:list")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<NewsType> entityWrapper = new QueryWrapper<>();
        entityWrapper.orderByDesc(false, "create_date");
        String newsTypeName = request.getParameter("newsTypeName");
        if (!StringUtils.isEmpty(newsTypeName)) {
            entityWrapper.like("newsType_name", newsTypeName);
        }
        // 预处理
        Page pageBean = newsTypeService.selectPage(getPage(), entityWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @RequiresPermissions("biz:newsType:newstype:add")
    public String add(NewsType entity, BindingResult result,
                      HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        newsTypeService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @RequiresPermissions("biz:newsType:newstype:update")
    public String update(NewsType entity, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        newsTypeService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @RequiresPermissions("biz:newsType:newstype:detail")
    public String detail(Model model, @PathVariable("id") String id) {
        NewsType newsType = newsTypeService.selectById(id);
        return Response.successJson(newsType);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @RequiresPermissions("biz:newsType:newstype:delete")
    public String batchDelete(@RequestParam("ids") String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        newsTypeService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }


    @GetMapping(value = "/select")
    @Log(logType = LogType.SELECT)
    @RequiresPermissions("biz:newsType:newstype:list")
    public String selectNewsContentType(){
        QueryWrapper<NewsType> entityWrapper = new QueryWrapper<>();
        entityWrapper.orderByDesc(false, "create_date");
        List<NewsType> NewsType = newsTypeService.selectList(entityWrapper);
        String s = Response.successJson(NewsType);

        return s;
    }
}
