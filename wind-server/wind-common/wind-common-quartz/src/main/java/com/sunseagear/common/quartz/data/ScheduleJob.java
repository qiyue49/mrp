package com.sunseagear.common.quartz.data;

import lombok.Data;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @title: ScheduleJob.java
 * @package com.sunseagear.core.quartz.domain
 * @description: 计划任务信息 * @date: 2017年5月9日 下午10:39:13
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@Data
public class ScheduleJob implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long jobId;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务分组
     */
    private String jobGroup;
    /**
     * 任务状态 是否启动任务
     */
    private String jobStatus;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 类加载方式
     */
    private String loadWay;
    /**
     * 描述
     */
    private String description;
    /**
     * 任务是否有状态
     */
    private String isConcurrent;

    /**
     * 任务调用的方法名
     */
    private String methodName;

    /**
     * 任务调用的参数
     */
    private String methodParams;
    /**
     * 执行策略
     */
    private String misfirePolicy;

    private String executeClass;
}
