package com.sunseagear.wind.modules.biz.event.service.impl;

import com.sunseagear.common.mvc.service.impl.CommonServiceImpl;
import com.sunseagear.wind.modules.biz.event.service.IEventService;
import com.sunseagear.wind.modules.biz.event.entity.Event;
import com.sunseagear.wind.modules.biz.event.mapper.EventMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
@Transactional
@Service("eventService")
public class EventServiceImpl  extends CommonServiceImpl<EventMapper, Event> implements  IEventService {

}
