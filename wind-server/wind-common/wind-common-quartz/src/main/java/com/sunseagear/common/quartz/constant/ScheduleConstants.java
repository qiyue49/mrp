package com.sunseagear.common.quartz.constant;

public enum ScheduleConstants {
    TASK_JOB_KEY_PRE("TASK_JOB_KEY_PRE"),
    TASK_TRIGGER_KEY_PRE("TASK_TRIGGER_KEY_PRE"),
    TASK_JOB_BAEN_KEY("TASK_JOB_BAEN_KEY"),

    MISFIRE_DEFAULT("0", "默认"),
    MISFIRE_IGNORE_MISFIRES("1", "立即触发执行"),
    MISFIRE_FIRE_AND_PROCEED("2", "触发一次执行"),
    MISFIRE_DO_NOTHING("3", "不触发立即执行"),

    STATUS_RUNNING("1", "运行中"),
    STATUS_NOT_RUNNING("0", "未运行"),
    CONCURRENT_IS("1", "允许并发"),
    CONCURRENT_NOT("0", "禁止并发");

    private String value;
    private String description;

    ScheduleConstants(String value) {
        this.value = value;
    }

    ScheduleConstants(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public static ScheduleConstants fromValue(String value) {
        for (ScheduleConstants constant : values()) {
            if (constant.getValue().equals(value)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
