package com.sunseagear.wind.modules.biz.employee.service.impl;

import com.sunseagear.common.mvc.service.impl.CommonServiceImpl;
import com.sunseagear.wind.modules.biz.employee.entity.Employee;
import com.sunseagear.wind.modules.biz.employee.mapper.EmployeeMapper;
import com.sunseagear.wind.modules.biz.employee.service.IEmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.employee
 * @title: 员工信息&mdash;&mdash;左树右表主表（飞廉演示）控制器
 * @description: 员工信息&mdash;&mdash;左树右表主表（飞廉演示）控制器
 * @author:
 * @date: 2021-05-20 14:04:18
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */
@Transactional
@Service("employeeService")
public class EmployeeServiceImpl extends CommonServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
