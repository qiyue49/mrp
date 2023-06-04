package com.sunseagear.wind.modules.biz.department.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.TreeEntity;
import com.sunseagear.wind.modules.sys.entity.User;
import lombok.Data;

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

@Data
@TableName("biz_department")
@SuppressWarnings("serial")
public class Department extends TreeEntity<Long> {


    @TableField(value = "code")
    private String code;  //编码
    @TableField(value = "manager")
    private String manager;  //经理
    @TableField(value = "address")
    private String address;  //地址
    @TableField(exist = false)
    private User user;  //用户

}
