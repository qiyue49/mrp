package com.sunseagear.common.quartz.constant;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.common.quartz.constant
 * @title:
 * @description: 任务调度排序 * @date: 2018/9/17 15:48
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
public interface ScheduleConstants {

    String TASK_JOB_KEY_PRE_ = "TASK_JOB_KEY_PRE_";

    String TASK_TRIGGER_KEY_PRE_ = "TASK_TRIGGER_KEY_PRE_";

    String TASK_JOB_BAEN_KEY = "TASK_JOB_BAEN_KEY";

    /**
     * 默认
     */
    String MISFIRE_DEFAULT = "0";

    /**
     * 立即触发执行
     */
    String MISFIRE_IGNORE_MISFIRES = "1";

    /**
     * 触发一次执行
     */
    String MISFIRE_FIRE_AND_PROCEED = "2";

    /**
     * 不触发立即执行
     */
    String MISFIRE_DO_NOTHING = "3";


    String STATUS_RUNNING = "1";
    String STATUS_NOT_RUNNING = "0";
    String CONCURRENT_IS = "1";
    String CONCURRENT_NOT = "0";

}
