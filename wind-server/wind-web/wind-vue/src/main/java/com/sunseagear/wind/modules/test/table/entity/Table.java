package com.sunseagear.wind.modules.test.table.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import com.sunseagear.wind.modules.sys.entity.User;
import com.sunseagear.wind.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.bbs.modules.sys.entity
 * @title: 操作日志实体
 * @description: 操作日志实体 * @date: 2018-09-30 15:53:25
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@Data
@TableName("test_table")
@SuppressWarnings("serial")
public class Table extends DataEntity<Long> {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; //id

    @ExcelField(title = "标题", sort = 10)
    @TableField(value = "title")
    private String title; //标题

    @ExcelField(title = "内容", sort = 20)
    @TableField(value = "content")
    private String content; //内容

    @ExcelField(title = "作者", sort = 30)
    @TableField(value = "author")
    private String author; //作者

    @ExcelField(title = "类型", sort = 40)
    @TableField(value = "type")
    private String type; //类型

    @ExcelField(title = "状态", sort = 50)
    @TableField(value = "status")
    private String status; //状态

    @ExcelField(title = "重要程度", sort = 60)
    @TableField(value = "level")
    private Integer level; //重要程度

    @TableField(value = "tag")
    private String tag; //标签

    @TableField(value = "readings")
    private Integer readings; //阅读数

    @ExcelField(title = "发布时间", sort = 70)
    @TableField(value = "publish_date")
    private Date publishDate; //发布时间

    @TableField(exist = false)
    private User user; //发布时间
}
