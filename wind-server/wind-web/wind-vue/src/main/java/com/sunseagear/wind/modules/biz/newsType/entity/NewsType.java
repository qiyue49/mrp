package com.sunseagear.wind.modules.biz.newsType.entity;

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
 * @package biz.newsType
 * @title: 新闻类型控制器
 * @description: 新闻类型控制器
 * @author:
 * @date: 2020-11-09 04:00:26
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@Data
@TableName("biz_newsType")
@SuppressWarnings("serial")
public class NewsType extends DataEntity<Long> {


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; //id
    @TableField(value = "newsType_name")
    private String newsTypeName;  //新闻类型名称

    //
}
