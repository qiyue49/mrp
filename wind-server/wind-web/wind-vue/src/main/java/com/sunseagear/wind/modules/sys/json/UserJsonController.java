package com.sunseagear.wind.modules.sys.json;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.JsonUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.sys.entity.User;
import com.sunseagear.wind.modules.sys.service.IUserService;
import com.sunseagear.wind.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.sys.controller
 * @title: 党员信息控制器
 * @description: 党员信息控制器 * @date: 2018-09-03 15:10:10
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("json/user")
@Log(title = "用户管理")
public class UserJsonController extends BaseBeanController<User> {
    @Autowired
    private IUserService userService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    public String list(HttpServletRequest request) throws IOException {
        QueryWrapper<User> entityWrapper = new QueryWrapper<>();
        String tenantId = UserUtils.getTenantId();
        if (!StringUtils.isEmpty(tenantId)) {
            entityWrapper.eq("t.tenant_id", tenantId);
        }
        // 用于专家问答 在查询所有党员的时候，排除登陆人自己
        String search = request.getParameter("search");
        if (!StringUtils.isEmpty(search)) {
            entityWrapper.like("realname", search).or().like("t.phone", search);
        }
        // 预处理
        Page pageBean = userService.selectPage(getPage(), entityWrapper);
        return JsonUtils.successMessage(pageBean.getRecords(), "password,username,salt", false);
    }

    @PostMapping("login")
    public Object login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return JsonUtils.failMessage("请联系后台管理员");
        }
        Boolean result = userService.checkPassword(user.getId(), password);

        if (result) {
            return JsonUtils.successMessage("登陆成功!", user);
        }
        return JsonUtils.failMessage("账户名或密码不对");
    }

    @PostMapping(value = "changePassword")
    @Log(logType = LogType.OTHER, title = "修改成功")
    public String changePassword(String id, String password, String oldPassword) {
        if (userService.checkPassword(id, oldPassword)) {
            userService.changePassword(id, password);
            return JsonUtils.successMessage("密码修改成功");
        } else {
            return JsonUtils.failMessage("旧密码错误");
        }
    }

    /**
     * 通过用id查询用户信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail/{id}")
    @Log(logType = LogType.SELECT)
    public String selectUserById(@PathVariable("id") String id) {
        User user = userService.selectById(id);
        return JsonUtils.successMessage(user);
    }

}
