package com.sunseagear.wind.modules.biz.company.entity;

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
 * @package biz.company
 * @title: 供货厂家&mdash;&mdash;级联表主表（飞廉演示）控制器
 * @description: 供货厂家&mdash;&mdash;级联表主表（飞廉演示）控制器
 * @author:
 * @date: 2021-10-21 07:59:27
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@Data
@TableName("biz_company")
@SuppressWarnings("serial")
public class Company extends DataEntity<String> {


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id; //id
    @TableField(value = "name")
    private String name;  //名称
    @TableField(value = "contact")
    private String contact;  //联系人
    @TableField(value = "phone")
    private String phone;  //联系电话
    @TableField(value = "address")
    private String address;  //地址
}
