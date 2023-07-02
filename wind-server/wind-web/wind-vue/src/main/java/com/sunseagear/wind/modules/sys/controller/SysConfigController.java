package com.sunseagear.wind.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.common.helper.SysConfigHelper;
import com.sunseagear.wind.modules.sys.entity.SysConfig;
import com.sunseagear.wind.modules.sys.service.ISysConfigService;
import com.sunseagear.wind.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.sys.controller
 * @title: 消息模版控制器
 * @description: 消息模版控制器 * @date: 2018-09-03 15:10:10
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/sys/config")
@PreAuthorize("hasAuthority('sys:config')")
@Log(title = "系统配置")
public class SysConfigController extends BaseBeanController<SysConfig> {
    @Autowired
    private ISysConfigService sysConfigService;

    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:config:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_id", UserUtils.getTenantId());
        queryWrapper.orderByAsc("id");
        String code = request.getParameter("code");
        if (!StringUtils.isEmpty(code)) {
            queryWrapper.eq("code", code);
        }
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        // 预处理
        Page pageBean = sysConfigService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('sys:config:add')")
    public String add(@Valid SysConfig entity, BindingResult result) {
        long count = sysConfigService.selectCount(new QueryWrapper<SysConfig>().
                nested(i -> i.eq("name", entity.getName()).or().eq("code", entity.getName()))
                .eq("tenant_id", UserUtils.getTenantId()));
        if (count > 0) {
            return Response.failJson("名称和编码不能重复");
        }
        // 验证错误
        this.checkError(entity, result);
        entity.setTenantId(UserUtils.getTenantId());
        sysConfigService.insert(entity);
        SysConfigHelper.getInstance().update(UserUtils.getTenantId());
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('sys:config:update')")
    public String update(@Valid SysConfig entity, BindingResult result) {
        long count = sysConfigService.selectCount(new QueryWrapper<SysConfig>()
                .nested(i -> i.eq("name", entity.getName()).or().eq("code", entity.getName()))
                .eq("tenant_id", UserUtils.getTenantId())
                .ne("id", entity.getId()));
        if (count > 0) {
            return Response.failJson("名称和编码不能重复");
        }
        // 验证错误
        this.checkError(entity, result);
        sysConfigService.insertOrUpdate(entity);
        SysConfigHelper.getInstance().update(UserUtils.getTenantId());
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:config:detail')")
    public String detail(@PathVariable("id") Long id) {
        SysConfig tenant = sysConfigService.selectById(id);
        SysConfigHelper.getInstance().update(UserUtils.getTenantId());
        return Response.successJson(tenant);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('sys:config:delete')")
    public String delete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        sysConfigService.deleteBatchIds(idList);
        SysConfigHelper.getInstance().update(UserUtils.getTenantId());
        return Response.ok("删除成功");
    }

    @GetMapping("config")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:config:list')")
    public String config() {
        List<SysConfig> list = SysConfigHelper.getInstance().getSysConfigList(UserUtils.getTenantId());
        return Response.successJson(list);
    }


}
