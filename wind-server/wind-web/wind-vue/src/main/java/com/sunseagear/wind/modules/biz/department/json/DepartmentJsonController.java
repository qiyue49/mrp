package com.sunseagear.wind.modules.biz.department.json;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.JsonUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.common.helper.VueTreeHelper;
import com.sunseagear.wind.modules.biz.department.entity.Department;
import com.sunseagear.wind.modules.biz.department.service.IDepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.department
 * @title: 组织架构&mdash;&mdash;左树右表树表（飞廉演示）控制器
 * @description: 组织架构&mdash;&mdash;左树右表树表（飞廉演示）控制器
 * @author:
 * @date: 2021-04-15 10:23:14
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("json/department")
@Log(title = "组织架构&mdash;&mdash;左树右表树表（飞廉演示）")
public class DepartmentJsonController extends BaseBeanController<Department> {

    @Autowired
    private IDepartmentService departmentService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PostMapping(value = "list")
    @Log(logType = LogType.SELECT)
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        // 预处理
        List<Department> treeNodeList = departmentService.selectTreeList(queryWrapper);
        List<Department> vueTreeNodes = VueTreeHelper.create().sort(treeNodeList);
        return JsonUtils.successMessage(vueTreeNodes, "address", false);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    public String add(Department entity, BindingResult result,
                      HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        departmentService.insert(entity);
        return JsonUtils.successMessage("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    public String update(Department entity, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {
        // 验证错误
        this.checkError(entity, result);
        departmentService.insertOrUpdate(entity);
        return JsonUtils.successMessage("更新成功");
    }

    @GetMapping("detail/{id}")
    @Log(logType = LogType.SELECT)
    public String detail(Model model, @PathVariable("id") Long id) {
        Department department = departmentService.selectById(id);
        return JsonUtils.successMessage(department);
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    public String batchDelete(@RequestParam("ids") String[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        departmentService.deleteBatchIds(idList);
        return JsonUtils.successMessage("删除成功");
    }
}
