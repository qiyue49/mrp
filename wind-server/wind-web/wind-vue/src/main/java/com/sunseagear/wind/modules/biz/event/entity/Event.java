package com.sunseagear.wind.modules.biz.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import com.sunseagear.wind.modules.sys.entity.Organization;
import com.sunseagear.wind.modules.sys.entity.User;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.event
 * @title: 事件管理&mdash;&mdash;单表（飞廉演示）控制器
 * @description: 事件管理&mdash;&mdash;单表（飞廉演示）控制器
 * @author:
 * @date: 2021-04-12 01:03:40
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@Data
@TableName("biz_event")
@SuppressWarnings("serial")
public class Event extends DataEntity<Long> {


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; //id
    @TableField(value = "title")
    private String title;  //标题
    @TableField(value = "user_id")
    private Long userId;  //用户
    @TableField(value = "organization_id")
    private String organizationId;  //部门
    @TableField(value = "date")
    private Date date;  //日期
    @TableField(value = "type")
    private String type;  //类型
    @TableField(value = "content")
    private String content;  //内容
    @TableField(value = "location")
    private String location;  //位置
    @TableField(value = "image")
    private String image;  //图片
    @TableField(exist = false)
    private User user;  //用户
    @TableField(exist = false)
    private Organization organization;  //所属机构
}
