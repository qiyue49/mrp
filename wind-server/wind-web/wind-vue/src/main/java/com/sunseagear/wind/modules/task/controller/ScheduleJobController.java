package com.sunseagear.wind.modules.task.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunseagear.common.http.Response;
import com.sunseagear.common.mvc.controller.BaseBeanController;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.wind.aspectj.annotation.Log;
import com.sunseagear.wind.aspectj.enums.LogType;
import com.sunseagear.wind.modules.task.entity.ScheduleJob;
import com.sunseagear.wind.modules.task.service.IScheduleJobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


@RestController
@RequestMapping("/task/schedule/job")
@PreAuthorize("hasAuthority('task:schedule:job')")
@Log(title = "计划任务")
public class ScheduleJobController extends BaseBeanController<ScheduleJob> {

    @Autowired
    private IScheduleJobService scheduleJobService;


    @GetMapping(value = "list")
    @Log(logType = LogType.SELECT)
    @PreAuthorize("hasAuthority('task:schedule:job:list')")
    public String list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<ScheduleJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        String jobName = request.getParameter("jobName");
        if (!StringUtils.isEmpty(jobName)) {
            queryWrapper.like("job_name", jobName);
        }
        // 预处理
        Page pageBean = scheduleJobService.selectPage(getPage(), queryWrapper);
        return Response.successPageJson(pageBean);
    }

    @PostMapping("add")
    @Log(logType = LogType.INSERT)
    @PreAuthorize("hasAuthority('task:schedule:job:add')")
    public String add(@Valid ScheduleJob entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        if (!CronExpression.isValidExpression(entity.getCronExpression())) {
            return Response.error("cron表达式格式不对");
        }
        scheduleJobService.insert(entity);
        return Response.ok("添加成功");
    }

    @PostMapping("update")
    @Log(logType = LogType.UPDATE)
    @PreAuthorize("hasAuthority('task:schedule:job:update')")
    public String update(@Valid ScheduleJob entity, BindingResult result) {
        // 验证错误
        this.checkError(entity, result);
        if (!CronExpression.isValidExpression(entity.getCronExpression())) {
            return Response.error("cron表达式格式不对");
        }
        scheduleJobService.insertOrUpdate(entity);
        return Response.ok("更新成功");
    }

    @PostMapping("delete")
    @Log(logType = LogType.DELETE)
    @PreAuthorize("hasAuthority('task:schedule:job:delete')")
    public String batchDelete(@RequestParam("ids") Long[] ids) {
        List<Serializable> idList = java.util.Arrays.asList(ids);
        scheduleJobService.deleteBatchIds(idList);
        return Response.ok("删除成功");
    }

    @PostMapping(value = "{id}/changeJobStatus")
    @Log(logType = LogType.OTHER, title = "任务状态")
    @PreAuthorize("hasAuthority('task:schedule:job:change:job:status')")
    public String changeJobStatus(@PathVariable("id") Long id, HttpServletRequest request) {
        String cmd = request.getParameter("cmd");
        String label = "停止";
        if (cmd.equals("start")) {
            label = "启动";
        } else {
            label = "停止";
        }
        try {
            scheduleJobService.changeStatus(id, cmd);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("任务" + label + "失败" + e.getMessage());
        }
        return Response.ok("任务" + label + "成功");
    }

    @PostMapping(value = "{id}/updateCron")
    @Log(logType = LogType.OTHER, title = "任务更新")
    @PreAuthorize("hasAuthority('task:schedule:job:refresh:job')")
    public String updateCron(@PathVariable("id") Long id) {
        ScheduleJob scheduleJob = scheduleJobService.selectById(id);
        if (scheduleJob == null) {
            return Response.failJson("出错了 定时任务未找到");
        }
        if (scheduleJob.getJobStatus().equals(ScheduleJob.STATUS_DISABLE)) {
            return Response.failJson("该定时任务未开启");
        }
        scheduleJobService.updateCron(id);
        return Response.ok("任务更新成功");
    }

    @PostMapping(value = "/runAJobNow")
    @Log(logType = LogType.OTHER, title = "执行一次")
    @PreAuthorize("hasAuthority('task:schedule:job:change:job:status')")
    public String runAJobNow(ScheduleJob scheduleJob) {
        if (scheduleJob == null) {
            return Response.failJson("出错了");
        }
        if (scheduleJob.getJobStatus().equals(ScheduleJob.STATUS_DISABLE)) {
            return Response.failJson("请开启该定时任后在执行");
        }
        scheduleJobService.runAJobNow(scheduleJob.getId());
        return Response.ok("任务启动成功");
    }

    /**
     * 刷新任务
     *
     * @return
     */
    @PostMapping(value = "/refreshJob")
    @Log(logType = LogType.OTHER, title = "刷新任务")
    @PreAuthorize("hasAuthority('task:schedule:job:refresh:job')")
    public String refreshJob() {
        scheduleJobService.refreshTask();
        return Response.ok("刷新任务成功");
    }
}
