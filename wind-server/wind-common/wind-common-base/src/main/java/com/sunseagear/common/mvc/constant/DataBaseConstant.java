package com.sunseagear.common.mvc.constant;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @title: DataBaseConstant.java
 * @package com.sunseagear.common.modules.common
 * @description: 常量 * @date: 2017年5月29日 下午9:55:00
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
public enum DataBaseConstant {
    // 正常
    NORMAL(0, "正常"),
    // 删除
    DELETE(1, "删除");

    private int value;
    private String description;

    DataBaseConstant(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static DataBaseConstant fromValue(int value) {
        for (DataBaseConstant constant : values()) {
            if (constant.getValue() == value) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Invalid delete flag value: " + value);
    }
}
