package com.sunseagear.wind.modules.biz.newsContent.json;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.JsonUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.biz.newsContent.entity.NewsContent;
import com.sunseagear.wind.modules.biz.newsContent.service.INewsContentService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("json/newsContent")
@Log(title = "新闻内容")
public class NewsContentJsonController extends BaseBeanController<NewsContent> {

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
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<NewsContent> entityWrapper = new QueryWrapper<>();
        entityWrapper.orderByDesc(false, "create_date");
        String newsTypeId = request.getParameter("newsTypeId");
        if (!StringUtils.isEmpty(newsTypeId)) {
            entityWrapper.eq("newsType_id", newsTypeId);
        }

        // 预处理
        Page pageBean = newsContentService.selectPage(getPage(), entityWrapper);
        return JsonUtils.successMessage(pageBean.getRecords());
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    public String add(NewsContent entity, BindingResult result,
                      HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        newsContentService.insert(entity);
        return JsonUtils.successMessage("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    public String update(NewsContent entity, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        newsContentService.insertOrUpdate(entity);
        return JsonUtils.successMessage("更新成功");
    }

    @GetMapping("detail/{id}/{userId}")
    @Log(logType = LogType.SELECT)
    public String detail(@PathVariable("id") String id, @PathVariable("userId") String userId) {
        NewsContent newsContent = newsContentService.selectById(id);
        return JsonUtils.successMessage(newsContent);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    public String batchDelete(@RequestParam("ids") String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        newsContentService.deleteBatchIds(idList);
        return JsonUtils.successMessage("删除成功");
    }

    @PostMapping("collection")
    @Log(logType = LogType.DELETE)
    public String collection(String userId, String newsId) {
        if (newsContentService.isCollection(userId, newsId)) {
            newsContentService.removeCollection(userId, newsId);
            return JsonUtils.successMessage("取消收藏成功");
        } else {
            newsContentService.insertCollection(userId, newsId);
            return JsonUtils.successMessage("收藏成功");
        }

    }

    @PostMapping("myCollection")
    @Log(logType = LogType.DELETE)
    public String myCollection(String userId) {
        QueryWrapper<NewsContent> entityWrapper = new QueryWrapper<>();
        entityWrapper.orderByDesc(false, "create_date");
        entityWrapper.eq("c.user_id", userId);
        // 预处理
        Page pageBean = newsContentService.myCollection(getPage(), entityWrapper);
        return JsonUtils.successMessage(pageBean.getRecords(), "newsContentDescribe", false);

    }

    @PostMapping("isCollection")
    @Log(logType = LogType.DELETE)
    public String isCollection(@RequestParam(required = true) String userId, @RequestParam(required = true) String newsId) {
        return JsonUtils.successMessage(newsContentService.isCollection(userId, newsId));
    }
}
