package com.sunseagear.wind.modules.biz.event.mapper;

import com.sunseagear.wind.modules.biz.event.entity.Event;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.event
 * @title: 事件管理&mdash;&mdash;单表（飞廉演示）控制器
 * @description: 事件管理&mdash;&mdash;单表（飞廉演示）控制器
 * @author: 
 * @date: 2021-04-12 01:03:40
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */
@Mapper
public interface EventMapper extends BaseMapper<Event> {

}
