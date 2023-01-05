package com.sunseagear.wind.modules.biz.event.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.biz.event.service.IEventService;
import com.sunseagear.wind.modules.biz.event.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.io.IOException;
import java.util.List;


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

@RestController
@RequestMapping("biz/event/event")
@RequiresPermissions("biz:event:event")
@Log(title = "事件管理&mdash;&mdash;单表（飞廉演示）")
public class EventController extends BaseBeanController<Event> {

    @Autowired
    private IEventService eventService;
    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @RequiresPermissions("biz:event:event:list")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String title = request.getParameter("title");
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }
        String userId = request.getParameter("userId");
        if (!StringUtils.isEmpty(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        String type = request.getParameter("type");
        if (!StringUtils.isEmpty(type)) {
            queryWrapper.eq("type", type);
        }
        // 预处理
        Page pageBean = eventService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean, "content", false);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @RequiresPermissions("biz:event:event:add")
    public String add(Event entity, BindingResult result,
                      HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        eventService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @RequiresPermissions("biz:event:event:update")
    public String update(Event entity, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        eventService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @RequiresPermissions("biz:event:event:detail")
    public String detail(Model model, @PathVariable("id") String id) {
        Event event = eventService.selectById(id);
        return Response.successJson(event);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @RequiresPermissions("biz:event:event:delete")
    public String batchDelete(@RequestParam("ids") String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        eventService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
