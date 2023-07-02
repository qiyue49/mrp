package com.sunseagear.wind.modules.email.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.email.entity.EmailSendLog;
import com.sunseagear.wind.modules.email.service.IEmailSendLogService;
import com.sunseagear.wind.modules.email.service.IEmailSendService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.email.controller
 * @title: 邮件发送日志控制器
 * @description: 邮件发送日志控制器 * @date: 2018-09-12 10:58:46
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/email/sendlog")
@PreAuthorize("hasAuthority('email:sendlog')")
@Log(title = "邮件发送日志")
public class EmailSendLogController extends BaseBeanController<EmailSendLog> {

    @Autowired
    private IEmailSendLogService emailSendLogService;
    @Autowired
    private IEmailSendService emailSendService;


    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('email:sendlog:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<EmailSendLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("response_date");
        String subject = request.getParameter("subject");
        if (!StringUtils.isEmpty(subject)) {
            queryWrapper.like("subject", subject);
        }
        String email = request.getParameter("email");
        if (!StringUtils.isEmpty(email)) {
            queryWrapper.eq("email", email);
        }
        String status = request.getParameter("status");
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status", status);
        }
        // 预处理
        Page pageBean = emailSendLogService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('email:sendlog:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        emailSendLogService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    @PostMapping(value = "retrySend")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('email:sendlog:list')")
    public String retrySend(@RequestParam(value = "ids", required = false) Long[] ids) {
        try {
            List<Long> idList = java.util.Arrays.asList(ids);
            emailSendService.retrySend(idList);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("重发队列添加失败");
        }
        return Response.ok("重发队列添加成功");
    }
}
