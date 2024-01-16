package com.sunseagear.common.datarule.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DataRuleModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_FIELD = "organizationId";
    public static final String DEFAULT_COLUMN = "organization_id";
    public static final String DEFAULT_TABLE = "sys_organization";

    public static final int ALL = 1; //  全部
    public static final int OWN = 2; //  本人可见
    public static final int OWN_ORG = 3; //  所在机构可见
    public static final int OWN_ORG_CHILDREN = 4; //  所在机构及子级可见
    public static final int CUSTOM = 5; //  自定义


    private String id;

    /**
     * 数据权限字段
     */
    private String scopeColumn = DEFAULT_COLUMN;
    private String userColumn;
    private String userEntityField = DEFAULT_FIELD;

    //数据权限关联表名
    private String tableName = DEFAULT_TABLE;
    /**
     * 数据权限规则
     */
    private Integer scopeType = ALL;
    /**
     * 可见字段
     */
    private String scopeField;
    /**
     * 数据权限规则值
     */
    private String scopeValue;

    private String scopeClass;

}
