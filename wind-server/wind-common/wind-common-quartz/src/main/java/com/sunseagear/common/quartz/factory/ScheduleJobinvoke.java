package com.sunseagear.common.quartz.factory;

import com.sunseagear.common.quartz.data.ScheduleJob;
import com.sunseagear.common.quartz.exception.QuartzException;
import com.sunseagear.common.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScheduleJobinvoke {

    /**
     * 通过反射调用scheduleJob中定义的方法
     */
    public static void invokeMethod(ScheduleJob scheduleJob) {
        try {
            Object object = null;
            Class<?> clazz = null;
            Method method = null;
            if ("1".equals(scheduleJob.getLoadWay())) {
                object = SpringContextHolder.getBean(scheduleJob.getExecuteClass());
            } else if (StringUtils.isNotBlank(scheduleJob.getExecuteClass())) {
                object = Class.forName(scheduleJob.getExecuteClass()).getDeclaredConstructor().newInstance();
            }
            if (object == null) {
                throw new QuartzException("任务对象不存在");
            }
            clazz = object.getClass();
            method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
            String params = scheduleJob.getMethodParams();
            ReflectionUtils.makeAccessible(method);
            if (StringUtils.isNotEmpty(params)) {
                method.invoke(object, params);
            } else {
                method.invoke(object);
            }
        } catch (InstantiationException | InvocationTargetException | IllegalArgumentException |
                 IllegalAccessException | SecurityException | NoSuchMethodException | ClassNotFoundException e) {
            throw new QuartzException(e.getMessage());
        }
    }
}
