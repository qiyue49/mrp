package com.sunseagear.wind.modules.biz.department.service.impl;

import com.sunseagear.common.mvc.service.impl.TreeCommonServiceImpl;
import com.sunseagear.wind.modules.biz.department.entity.Department;
import com.sunseagear.wind.modules.biz.department.mapper.DepartmentMapper;
import com.sunseagear.wind.modules.biz.department.service.IDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
@Transactional
@Service("departmentService")
public class DepartmentServiceImpl extends TreeCommonServiceImpl<DepartmentMapper, Department, Long> implements IDepartmentService {

}
