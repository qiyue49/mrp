package com.sunseagear.wind.modules.biz.department.mapper;

import com.sunseagear.wind.modules.biz.department.entity.Department;
import com.sunseagear.common.mvc.mapper.BaseTreeMapper;
import org.apache.ibatis.annotations.Mapper;

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
@Mapper
public interface DepartmentMapper extends BaseTreeMapper<Department> {

}
