package com.sunseagear.wind.modules.email.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.email.entity.EmailTemplate;
import com.sunseagear.wind.modules.email.service.IEmailTemplateService;
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
 * @package com.sunseagear.wind.modules.email.controller
 * @title: 邮件模板控制器
 * @description: 邮件模板控制器 * @date: 2018-09-12 10:59:18
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/email/template")
@PreAuthorize("hasAuthority('email:template')")
@Log(title = "邮件模板")
public class EmailTemplateController extends BaseBeanController<EmailTemplate> {

    @Autowired
    private IEmailTemplateService emailTemplateService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('email:template:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<EmailTemplate> queryWrapper = new QueryWrapper<>();
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
        Page pageBean = emailTemplateService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('email:template:add')")
    public String add(@Valid EmailTemplate entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        String templateCode = StringUtils.randomString(10);
        entity.setCode(templateCode);
        emailTemplateService.insert(entity);
        return Response.ok("添加成功");
    }


    @PostMapping("update")
    @PreAuthorize("hasAuthority('email:template:update')")
    @Log(logType = LogType.UPDATE)
    public String update(@Valid EmailTemplate entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        emailTemplateService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('email:template:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = Arrays.asList(ids);
        emailTemplateService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
