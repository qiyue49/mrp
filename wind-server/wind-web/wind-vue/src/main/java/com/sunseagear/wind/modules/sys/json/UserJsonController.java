package com.sunseagear.wind.modules.sys.json;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.sys.entity.User;
import com.sunseagear.wind.modules.sys.service.IUserService;
import com.sunseagear.wind.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.sys.controller
 * @title: 用户信息控制器
 * @description: 用户信息控制器 * @date: 2018-09-03 15:10:10
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("json/user")
@Log(title = "用户管理")
public class UserJsonController extends BaseBeanController<User> {
    @Autowired
    private IUserService userService;

    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    public String list(HttpServletRequest request) throws IOException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String tenantId = UserUtils.getTenantId();
        if (!StringUtils.isEmpty(tenantId)) {
            queryWrapper.eq("t.tenant_id", tenantId);
        }
        String search = request.getParameter("search");
        if (!StringUtils.isEmpty(search)) {
            queryWrapper.like("realname", search).or().like("phone", search);
        }
        // 预处理
        Page page = userService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(page, "password,username", false);
    }

    @PostMapping("login")
    public Object login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return Response.error("请联系后台管理员");
        }
        Boolean result = userService.checkPassword(user.getId(), password);

        if (result) {
            return Response.successJson("登陆成功!", user);
        }
        return Response.error("账户名或密码不对");
    }

    @PostMapping(value = "changePassword")
    @Log(logType = LogType.OTHER, title = "修改成功")
    public String changePassword(Long id, String password, String oldPassword) {
        if (userService.checkPassword(id, oldPassword)) {
            userService.changePassword(id, password);
            return Response.successJson("密码修改成功");
        } else {
            return Response.error("旧密码错误");
        }
    }

    @GetMapping(value = "/detail/{id}")
    @Log(logType = LogType.SELECT)
    public String selectUserById(@PathVariable("id") Long id) {
        User user = userService.selectById(id);
        return Response.successJson(user);
    }

}
