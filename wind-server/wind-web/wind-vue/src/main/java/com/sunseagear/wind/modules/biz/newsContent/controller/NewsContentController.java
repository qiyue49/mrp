package com.sunseagear.wind.modules.biz.newsContent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.biz.newsContent.entity.NewsContent;
import com.sunseagear.wind.modules.biz.newsContent.service.INewsContentService;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * @package biz.newsContent
 * @title: 新闻内容控制器
 * @description: 新闻内容控制器
 * @author:
 * @date: 2020-11-09 03:59:45
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("biz/newsContent/newscontent")
@PreAuthorize("hasAuthority('biz:newsContent:newscontent')")
@Log(title = "新闻内容")
public class NewsContentController extends BaseBeanController<NewsContent> {

    @Autowired
    private INewsContentService newsContentService;
    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('biz:newsContent:newscontent:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<NewsContent> entityWrapper = new QueryWrapper<>();

        entityWrapper.orderByDesc(false, "create_date");
        String newsContentTitle = request.getParameter("newsContentTitle");
        if (!StringUtils.isEmpty(newsContentTitle)) {
            entityWrapper.like("newsContent_title", newsContentTitle);
        }
        String newsTypeId = request.getParameter("newsTypeId");
        if (!StringUtils.isEmpty(newsTypeId)) {
            entityWrapper.like("newsType_id", newsTypeId);
        }
        System.out.println(request.getParameter("newsTypeId"));

        // 预处理
        Page pageBean = newsContentService.selectPage(getPage(), entityWrapper);
        System.out.println(pageBean);
        return Response.successPageJson(pageBean,"newsContentDescribe",false);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('biz:newsContent:newscontent:add')")
    public String add(NewsContent entity, BindingResult result,
                      HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        newsContentService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('biz:newsContent:newscontent:update')")
    public String update(NewsContent entity, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        newsContentService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('biz:newsContent:newscontent:detail')")
    public String detail(Model model, @PathVariable("id") String id) {
        NewsContent newsContent = newsContentService.selectById(id);
        return Response.successJson(newsContent);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('biz:newsContent:newscontent:delete')")
    public String batchDelete(@RequestParam("ids") String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        newsContentService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }



}
