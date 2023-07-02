package com.sunseagear.wind.modules.monitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.tenant.TenantProperties;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.monitor.entity.OperationLog;
import com.sunseagear.wind.modules.monitor.service.IOperationLogService;
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
 * @package com.sunseagear.wind.modules.sys.controller
 * @title: 操作日志控制器
 * @description: 操作日志控制器 * @date: 2018-09-30 15:53:25
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("monitor/operation/log")
@PreAuthorize("hasAuthority('monitor:operation:log')")
@Log(title = "操作日志")
public class OperationLogController extends BaseBeanController<OperationLog> {

    @Autowired
    private IOperationLogService operationLogService;

    static {
        TenantProperties.getInstance().getIgnoreTables().add("sys_operation_log");
    }

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('monitor:operation:log:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String operationIp = request.getParameter("operationIp");
        if (!StringUtils.isEmpty(operationIp)) {
            queryWrapper.eq("operation_ip", operationIp);
        }
        String requestUri = request.getParameter("requestUri");
        if (!StringUtils.isEmpty(requestUri)) {
            queryWrapper.like("request_uri", requestUri);
        }

        // 预处理
        Page pageBean = operationLogService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('monitor:operation:log:detail')")
    public String detail(@PathVariable("id") Long id) {
        OperationLog operationLog = operationLogService.selectById(id);
        return Response.successJson(operationLog);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('monitor:operation:log:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        operationLogService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
