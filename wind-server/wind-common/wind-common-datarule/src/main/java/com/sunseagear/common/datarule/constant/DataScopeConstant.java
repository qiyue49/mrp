package com.sunseagear.common.datarule.constant;

public enum DataScopeConstant {
    /**
     * 数据权限字段
     */
    DEFAULT_FIELD("organizationId"),
    DEFAULT_COLUMN("organization_id"),
    DEFAULT_TABLE("sys_organization");

    private String value;

    DataScopeConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
