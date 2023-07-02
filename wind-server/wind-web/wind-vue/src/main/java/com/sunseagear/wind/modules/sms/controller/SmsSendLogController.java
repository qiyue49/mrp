package com.sunseagear.wind.modules.sms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.sms.entity.SmsSendLog;
import com.sunseagear.wind.modules.sms.service.ISmsSendLogService;
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
 * @package com.sunseagear.wind.modules.sms.controller
 * @title: 发送日志控制器
 * @description: 发送日志控制器 * @date: 2018-09-14 09:47:53
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("sms/sendlog")
@PreAuthorize("hasAuthority('sms:sendlog')")
@Log(title = "短信发送日志")
public class SmsSendLogController extends BaseBeanController<SmsSendLog> {

    @Autowired
    private ISmsSendLogService smsSendLogService;


    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sms:sendlog:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<SmsSendLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("response_date");
        String sendCode = request.getParameter("sendCode");
        if (!StringUtils.isEmpty(sendCode)) {
            queryWrapper.eq("send_code", sendCode);
        }
        String phone = request.getParameter("phone");
        if (!StringUtils.isEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        String status = request.getParameter("status");
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status", status);
        }
        // 预处理
        Page pageBean = smsSendLogService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('sms:sendlog:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        smsSendLogService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    @PostMapping(value = "retrySend")
    public String retrySend(@RequestParam(value = "ids", required = false) String[] ids) {
        try {
            List<Serializable> idList = java.util.Arrays.asList(ids);
            smsSendLogService.retrySend(idList);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("重发队列添加失败");
        }
        return Response.ok("重发队列添加成功");
    }
}
