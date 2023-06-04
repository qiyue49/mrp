package com.sunseagear.wind.modules.biz.employee.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import lombok.Data;

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

@Data
@TableName("biz_employee")
@SuppressWarnings("serial")
public class Employee extends DataEntity<Long> {


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; //id
    @TableField(value = "name")
    private String name;  //姓名
    @TableField(value = "sex")
    private String sex;  //性别
    @TableField(value = "birthday")
    private String birthday;  //生日
    @TableField(value = "card_number")
    private String cardNumber;  //工号
    @TableField(value = "org_id")
    private String orgId;  //所属部门
    @TableField(value = "avatar")
    private String avatar;  //照片
}
