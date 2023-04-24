package com.sunseagear.wind.modules.sys.json;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.JsonUtils;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.common.helper.VueTreeHelper;
import com.sunseagear.wind.modules.sys.entity.Organization;
import com.sunseagear.wind.modules.sys.service.impl.OrganizationServiceImpl;
import com.sunseagear.wind.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.partyCongress
 * @title: 党代会议控制器
 * @description: 党代会议控制器
 * @author:
 * @date: 2020-12-02 05:09:05
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("json/organization")
@Log(title = "组织机构")
public class OrganizationJsonController extends BaseBeanController<Organization> {

    @Autowired
    private OrganizationServiceImpl organizationService;

    @PostMapping("/list")
    @Log(logType = LogType.SELECT)
    public String List() {
        QueryWrapper<Organization> entityWrapper = new QueryWrapper<>();
        String tenantId = UserUtils.getTenantId();
        if (!StringUtils.isEmpty(tenantId)) {
            entityWrapper.eq("t.tenant_id", tenantId);
        }
        List<Organization> organizations = organizationService.selectTreeList(entityWrapper);
        List<Organization> sort = VueTreeHelper.create().sort(organizations);
        return JsonUtils.successMessage(sort);
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    public String detail(@PathVariable("id") String id) {
        Organization organization = organizationService.selectById(id);
        return JsonUtils.successMessage(organization);
    }

}
