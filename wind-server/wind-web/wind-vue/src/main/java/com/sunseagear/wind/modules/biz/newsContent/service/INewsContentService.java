package com.sunseagear.wind.modules.biz.newsContent.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.mvc.service.ICommonService;
import com.sunseagear.wind.modules.biz.newsContent.entity.NewsContent;

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
public interface INewsContentService extends ICommonService<NewsContent> {

    // 根据新闻内容id获取数据
    public NewsContent selectById(Long id);

    boolean isCollection(Long userId, String newsId);

    int removeCollection(Long userId, String newsId);

    int insertCollection(Long userId, String newsId);

    Page myCollection(Page page, Wrapper<NewsContent> wrapper);
}
