package com.sunseagear.wind.modules.sms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.sms.entity.SmsTemplate;
import com.sunseagear.wind.modules.sms.service.ISmsTemplateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.sms.controller
 * @title: 短信模板控制器
 * @description: 短信模板控制器 * @date: 2018-09-14 09:47:35
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("sms/template")
@PreAuthorize("hasAuthority('sms:template')")
@Log(title = "短信模板")
public class SmsTemplateController extends BaseBeanController<SmsTemplate> {

    @Autowired
    private ISmsTemplateService smsTemplateService;


    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sms:template:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<SmsTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String code = request.getParameter("code");
        if (!StringUtils.isEmpty(code)) {
            queryWrapper.like("code", code);
        }
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        // 预处理
        Page pageBean = smsTemplateService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('sms:template:add')")
    public String add(@Valid SmsTemplate entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        String templateCode = StringUtils.randomString(10);
        entity.setCode(templateCode);
        smsTemplateService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('sms:template:update')")
    public String update(@Valid SmsTemplate entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        smsTemplateService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('sms:template:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = Arrays.asList(ids);
        smsTemplateService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
