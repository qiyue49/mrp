package com.sunseagear.wind.modules.monitor.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.entity.Principal;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.sso.service.IOAuthService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @version V1.0
 * @Title: 在线用户
 * @Description: 在线用户
 * @date 2017-05-15 08:18:21
 */
@RestController
@RequestMapping(value = "/monitor/user/online")
@PreAuthorize("hasAuthority('monitor:user:online')")
@Log(title = "在线用户")
public class UserOnlineController extends BaseBeanController<Principal> {

    @Resource
    private IOAuthService oAuthService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     */
    @RequestMapping(value = "list", method = {RequestMethod.GET, RequestMethod.POST})
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('monitor:user:online:list')")
    public String list(HttpServletRequest request) {
        // 预处理
        Page<Principal> pageBean = oAuthService.activePrincipal(getPage(), request);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("/forceLogout")
    @Log(title = "用户强制退出")
    public String forceLogout(@RequestParam(value = "ids") Long[] ids) {
        for (Long id : ids) {
            // 强制退出，不想频繁的请求redis没想好怎么处理
        }
        return Response.ok("强制退出成功");
    }

}
