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
 * @title: 产品管理&mdash;&mdash;级联表子表（飞廉演示）控制器
 * @description: 产品管理&mdash;&mdash;级联表子表（飞廉演示）控制器
 * @author:
 * @date: 2021-10-21 07:59:27
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@Data
@TableName("biz_product")
@SuppressWarnings("serial")
public class Product extends DataEntity<Long> {


    @TableField(value = "name")
    private String name;  //名称
    @TableField(value = "price")
    private Double price;  //价格
    @TableField(value = "count")
    private Integer count;  //数量
    @TableField(value = "image")
    private String image;  //图片
    @TableField(value = "company_id")
    private String companyId;  //公司
}
