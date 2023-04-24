package com.sunseagear.wind.modules.biz.newsType.service.impl;

import com.sunseagear.common.mvc.service.impl.CommonServiceImpl;
import com.sunseagear.wind.modules.biz.newsType.entity.NewsType;
import com.sunseagear.wind.modules.biz.newsType.mapper.NewsTypeMapper;
import com.sunseagear.wind.modules.biz.newsType.service.INewsTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
@Transactional
@Service("newsTypeService")
public class NewsTypeServiceImpl extends CommonServiceImpl<NewsTypeMapper, NewsType> implements INewsTypeService {

}
