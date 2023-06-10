package com.sunseagear.wind.modules.task.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.AbstractEntity;
import lombok.Data;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.task.entity
 * @title: 任务日志实体
 * @description: 任务日志实体 * @date: 2018-09-17 14:25:19
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@Data
@TableName("task_schedule_job_log")
@SuppressWarnings("serial")
public class ScheduleJobLog extends AbstractEntity<Long> {

    public static final String SCHEDULE_JOB_LOG_RUN_SUCCESS = "1";

    public static final String SCHEDULE_JOB_LOG_RUN_NOMAL = "0";

    public static final String SCHEDULE_JOB_LOG_RUN_FAIL = "-1";
    /**
     * 任务日志ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 任务名称
     */
    @TableField(value = "job_name")
    private String jobName;
    /**
     * 任务组名
     */
    @TableField(value = "job_group")
    private String jobGroup;
    /**
     * Spring bean
     */
    @TableField(value = "execute_class")
    private String executeClass;
    /**
     * 任务方法
     */
    @TableField(value = "method_name")
    private String methodName;
    /**
     * 方法参数
     */
    @TableField(value = "method_params")
    private String methodParams;
    /**
     * 日志信息
     */
    @TableField(value = "job_message")
    private String jobMessage;
    /**
     * 执行状态（0正常 1失败）
     */
    @TableField(value = "status")
    private String status;
    /**
     * 异常信息
     */
    @TableField(value = "exception_info")
    private String exceptionInfo;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
