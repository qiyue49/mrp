package com.sunseagear.wind.modules.biz.newsContent.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.mvc.service.impl.CommonServiceImpl;
import com.sunseagear.wind.modules.biz.newsContent.service.INewsContentService;
import com.sunseagear.wind.modules.biz.newsContent.entity.NewsContent;
import com.sunseagear.wind.modules.biz.newsContent.mapper.NewsContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
@Transactional
@Service("newsContentService")
public class NewsContentServiceImpl extends CommonServiceImpl<NewsContentMapper, NewsContent> implements INewsContentService {

    /**
     * 根据新闻内容id获取数据,并且增加阅读量
     *
     * @param id
     * @return
     */

    @Autowired
    private NewsContentMapper newsContentMapper;

    @Override
    public NewsContent selectById(String id) {
        //获取数据库内容
        NewsContent newsContent = newsContentMapper.selectById(id);
        // 获取阅读量
        Integer readingsNumber = newsContent.getReadingsNumber();
        if (readingsNumber == null) {
            readingsNumber = 0;
        }
        //阅读量加1
        readingsNumber = readingsNumber + 1;
        newsContent.setReadingsNumber(readingsNumber);
        //更新数据到数据库
        newsContentMapper.updateById(newsContent);

        return newsContent;
    }

    @Override
    public boolean isCollection(String userId, String newsId) {
        return newsContentMapper.isCollection(userId, newsId);
    }

    @Override
    public int removeCollection(String userId, String newsId) {
        return newsContentMapper.removeCollection(userId, newsId);
    }

    @Override
    public int insertCollection(String userId, String newsId) {
        return newsContentMapper.insertCollection(userId, newsId);
    }

    @Override
    public Page myCollection(Page page, Wrapper<NewsContent> wrapper) {
        return newsContentMapper.myCollection(page, wrapper);
    }
}
