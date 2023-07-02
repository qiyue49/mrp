package com.sunseagear.wind.modules.sys.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.DateUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.common.response.ResponseError;
import com.sunseagear.wind.modules.sys.entity.User;
import com.sunseagear.wind.modules.sys.entity.UserRole;
import com.sunseagear.wind.modules.sys.service.IRoleService;
import com.sunseagear.wind.modules.sys.service.IUserRoleService;
import com.sunseagear.wind.modules.sys.service.IUserService;
import com.sunseagear.wind.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
@RequestMapping("/sys/user")
@PreAuthorize("hasAuthority('sys:user')")
@Log(title = "用户管理")
public class UserController extends BaseBeanController<User> {
    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleService roleService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @RequestMapping(value = "list", method = {RequestMethod.GET, RequestMethod.POST})
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:user:list')")
    public String list(HttpServletRequest request) throws IOException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("t.tenant_id", UserUtils.getTenantId());
        String realname = request.getParameter("realname");
        if (!StringUtils.isEmpty(realname)) {
            queryWrapper.like("t.realname", realname);
        }
        String username = request.getParameter("username");
        if (!StringUtils.isEmpty(username)) {
            queryWrapper.like("t.username", username);
        }
        String phone = request.getParameter("phone");
        if (!StringUtils.isEmpty(phone)) {
            queryWrapper.like("t.phone", phone);
        }
        String organizationId = request.getParameter("organization.id");
        if (!StringUtils.isEmpty(organizationId)) {
            queryWrapper.apply(String.format("t.organization_id = '%s' or b.parent_ids like '%%%s%%'", organizationId, organizationId));
        }
        // 预处理
        Page pageBean = userService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('sys:user:add')")
    public String add(@Valid User entity, BindingResult result,
                      HttpServletRequest request) {
        // 验证错误
        this.checkError(entity, result);
        //账号重复
        if (userService.selectCount(new QueryWrapper<User>().eq("username", entity.getUsername())) > 0) {
            return Response.failJson("账号重复");
        }
        entity.setTenantId(UserUtils.getTenantId());
        userService.insert(entity);
        //保存之后
        afterSave(entity, request);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('sys:user:update')")
    public String update(@Valid User entity, BindingResult result,
                         HttpServletRequest request) {
        // 验证错误
        this.checkError(entity, result);
        //账号重复
        if (userService.selectCount(new QueryWrapper<User>().ne("id", entity.getId()).eq("username", entity.getUsername())) > 0) {
            return Response.failJson("账号重复");
        }
        userService.update(entity);
        //保存之后
        afterSave(entity, request);
        return Response.ok("更新成功");
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        userService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    @PostMapping(value = "{id}/changePassword")
    @Log(logType = LogType.OTHER, title = "修改成功")
    @PreAuthorize("hasAuthority('sys:user:change:password')")
    public String changePassword(@PathVariable("id") Long id, HttpServletRequest request) {
        String password = request.getParameter("password");
        userService.changePassword(id, password);
        return Response.ok("密码修改成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:user:list')")
    public String detail(@PathVariable("id") Long id) {
        User user = userService.selectById(id);
        return Response.successJson(user);
    }

    public void afterSave(User entity, HttpServletRequest request) {
        // 删除角色关联
        String[] roleIdList = request.getParameterValues("roleIdList");
        if (roleIdList != null && roleIdList.length > 0) {
            userRoleService.delete(new QueryWrapper<UserRole>().eq("user_id", entity.getId()));
            List<UserRole> userRoleList = new ArrayList<UserRole>();
            for (String roleId : roleIdList) {
                UserRole userRole = new UserRole();
                userRole.setUserId(entity.getId());
                userRole.setRoleId(Long.parseLong(roleId));
                userRoleList.add(userRole);
            }
            userRoleService.insertBatch(userRoleList);
        }

    }

    @GetMapping("export")
    @Log(logType = LogType.EXPORT)
    @PreAuthorize("hasAuthority('sys:user:export')")
    public String export(HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            TemplateExportParams params = new TemplateExportParams(
                    "");
            //加入条件
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            // 子查询
            String organizationid = request.getParameter("organizationid");
            if (!StringUtils.isEmpty(organizationid)) {
                queryWrapper.eq("uo.organization_id", organizationid);
            }
            Page pageBean = userService.selectPage(getPage(), queryWrapper);
            String title = "用户信息";
            Workbook book = ExcelExportUtil.exportExcel(new ExportParams(
                    title, title, title), User.class, pageBean.getRecords());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            book.write(bos);
            byte[] bytes = bos.toByteArray();
            String bytesRes = StringUtils.bytesToHexString2(bytes);
            title = title + "-" + DateUtils.getDateTime();
            response.put("bytes", bytesRes);
            response.put("title", title);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResponseError.NORMAL_ERROR, "导出失败");
        }
        return Response.toJson(response, "导出成功");
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping(value = "info")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public String info() {
        User user = UserUtils.getUser();
        if (user == null) {
            return Response.failJson("获取失败");
        } else {
            user.setRoles(roleService.findListByUserId(user.getId()));
            return Response.successJson(user);
        }
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @param request
     * @return
     */
    @PostMapping("my/update")
    @Log(logType = LogType.UPDATE, title = "用户更新")
    public String myUpdate(User user, HttpServletRequest request) {
        Long userId = UserUtils.getUser().getId();
        User oldUser = userService.selectById(userId);
        // 验证错误
        BeanUtils.copyProperties(user, oldUser);
        userService.insertOrUpdate(oldUser);
        UserUtils.update(userId);
        return Response.ok("更新成功");
    }

    /**
     * 更新用户信息
     *
     * @param oldPassword
     * @param password
     * @param request
     * @return
     */
    @PostMapping("my/changePassword")
    @Log(logType = LogType.OTHER, title = "用户修改密码")
    public String myChangePassword(String oldPassword,
                                   String password, HttpServletRequest request) {
        Long userId = UserUtils.getUser().getId();
        if (userService.checkPassword(userId, oldPassword)) {
            userService.changePassword(userId, password);
        } else {
            return Response.error("原密码错误");
        }
        return Response.ok("密码修改成功");
    }
}
