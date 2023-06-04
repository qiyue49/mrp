package com.sunseagear.wind.modules.biz.newsContent.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.wind.modules.biz.newsContent.entity.NewsContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.newsContent
 * @title: 新闻内容Mapper
 * @description: 新闻内容Mapper
 * @author:
 * @date: 2020-11-09 03:59:45
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */
@Mapper
public interface NewsContentMapper extends BaseMapper<NewsContent> {

    boolean isCollection(Long userId, String newsId);

    int removeCollection(Long userId, String newsId);

    int insertCollection(Long userId, String newsId);

    Page<NewsContent> myCollection(Page<NewsContent> page, @Param(Constants.WRAPPER) Wrapper<NewsContent> wrapper);

}
