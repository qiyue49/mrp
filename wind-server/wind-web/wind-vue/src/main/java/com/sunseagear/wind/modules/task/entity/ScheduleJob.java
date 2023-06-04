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
 * @version V1.0
 * @Title: 任务
 * @Description: 任务
 * @date 2017-05-09 23:22:51
 */
@Data
@TableName("task_schedule_job")
public class ScheduleJob extends AbstractEntity<Long> {

    public static final String STATUS_ENABLE = "1";
    public static final String STATUS_DISABLE = "0";

    /**
     * 任务主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 任务名
     */
    @TableField(value = "job_name")
    private String jobName;
    /**
     * cron表达式
     */
    @TableField(value = "cron_expression")
    private String cronExpression;
    /**
     * Spring bean
     */
    @TableField(value = "execute_class")
    private String executeClass;
    /**
     * 任务调用的方法名
     */
    @TableField(value = "method_name")
    private String methodName;

    /**
     * 任务调用的参数
     */
    @TableField(value = "method_params")
    private String methodParams;
    /**
     * 执行策略
     */
    @TableField(value = "misfire_policy")
    private String misfirePolicy;

    @TableField(value = "load_way")
    private String loadWay;
    /**
     * 任务是否有状态
     */
    @TableField(value = "is_concurrent")
    private String isConcurrent;
    /**
     * 任务描述
     */
    @TableField(value = "description")
    private String description;
    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 任务状态
     */
    @TableField(value = "job_status")
    private String jobStatus;
    /**
     * 任务分组
     */
    @TableField(value = "job_group")
    private String jobGroup;
    /**
     * 更新时间
     */
    @TableField(value = "update_date")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

}
