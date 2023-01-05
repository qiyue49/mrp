package com.sunseagear.wind.modules.biz.newsContent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import lombok.Data;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package biz.newsContent
 * @title: 新闻内容控制器
 * @description: 新闻内容控制器
 * @author:
 * @date: 2020-11-09 03:59:45
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@Data
@TableName("biz_newsContent")
@SuppressWarnings("serial")
public class NewsContent extends DataEntity<String> {


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id; //id
    @TableField(value = "newsContent_title")
    private String newsContentTitle;  //内容标题
    @TableField(value = "newsContent_describe")
    private String newsContentDescribe;  //内容描述
    @TableField(value = "newsRelease_time")
    private Date newsReleaseTime;  //发布时间
    @TableField(value = "img")
    private String img;  //相关图片
    @TableField(value = "readings_number")
    private Integer readingsNumber;  //阅读数量
    @TableField(value = "newsType_id")
    private String newsTypeId;  //新闻类型id

    //在查询内容的时候，查询非数据库字段，newsTypeId
    @TableField(exist = false)
    private String newsTypeName;
}
