package com.sunseagear.wind.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.common.helper.VueTreeHelper;
import com.sunseagear.wind.modules.sys.entity.Organization;
import com.sunseagear.wind.modules.sys.service.IOrganizationService;
import com.sunseagear.wind.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


@RestController
@RequestMapping("/sys/organization")
@PreAuthorize("hasAuthority('sys:organization')")
@Log(title = "部门管理")
public class OrganizationController extends BaseBeanController<Organization> {

    @Autowired
    private IOrganizationService organizationService;


    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:organization:list')")
    public String list(HttpServletRequest request) throws IOException {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<Organization>();
        queryWrapper.eq("tenant_id", UserUtils.getTenantId());
        //加入条件
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        List<Organization> treeNodeList = organizationService.selectList(queryWrapper);
        List<Organization> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList);
        return Response.successJson(vueTreeNodes);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('sys:organization:add')")
    public String add(@Valid Organization entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        entity.setTenantId(UserUtils.getTenantId());
        organizationService.insert(entity);
        return Response.ok("添加成功");
    }


    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('sys:organization:update')")
    public String update(@Valid Organization entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        organizationService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('sys:organization:detail')")
    public String detail(@PathVariable("id") Long id) {
        Organization organization = organizationService.selectById(id);
        return Response.successJson(organization);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('sys:organization:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        organizationService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }
}
