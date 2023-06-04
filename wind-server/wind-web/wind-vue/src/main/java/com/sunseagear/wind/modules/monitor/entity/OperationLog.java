package com.sunseagear.wind.modules.monitor.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.sunseagear.common.mvc.entity.AbstractEntity;
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
@TableName("sys_operation_log")
@SuppressWarnings("serial")
public class OperationLog extends AbstractEntity<Long> {
    /**
     * 成功
     */
    public final static String OPERATION_LOG_SUCCESS = "1";
    /**
     * 失败
     */
    public final static String OPERATION_LOG_FAIL = "0";
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 日志标题
     */
    @TableField(value = "title")
    private String title;
    /**
     * 日志内容
     */
    @TableField(value = "content")
    private String content;
    /**
     * 操作方式
     */
    @TableField(value = "log_type")
    private String logType;
    /**
     * 请求URI
     */
    private String requestUri;
    /**
     * 浏览器
     */
    @TableField(value = "browser")
    private String browser;
    /**
     * 操作系统
     */
    @TableField(value = "os")
    private String os;
    /**
     * 请求URI
     */
    @TableField(value = "operation_ip")
    private String operationIp;
    @TableField(value = "operation_name")
    private String operationName;
    /**
     * 操作方法
     */
    @TableField(value = "method")
    private String method;
    /**
     * 数据
     */
    @TableField(value = "params")
    private String params;
    /**
     * 异常信息
     */
    @TableField(value = "msg")
    private String msg;
    /**
     * 请求状态
     */
    @TableField(value = "status")
    private String status;
    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
}
