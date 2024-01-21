package com.sunseagear.wind.modules.task.service;


import com.sunseagear.common.mvc.service.ICommonService;
import com.sunseagear.wind.modules.task.entity.ScheduleJob;

/**
 * @version V1.0
 * @Title: 任务
 * @Description: 任务
 * @date 2017-05-09 23:22:51
 */
public interface IScheduleJobService extends ICommonService<ScheduleJob> {
    /**
     * @title: initSchedule
     * @description: 初始化任务
     * @return: void
     */
    void initSchedule();

    /**
     * 更改状态
     */
    void changeStatus(Long jobId, String cmd);

    /**
     * 更改任务 cron表达式
     */
    void updateCron(Long jobId);

    /**
     * 执行一次
     */
    void runAJobNow(Long jobId);

    /**
     * 刷新任务
     */
    void refreshTask();
}
