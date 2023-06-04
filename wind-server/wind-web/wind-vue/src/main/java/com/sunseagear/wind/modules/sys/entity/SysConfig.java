package com.sunseagear.wind.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import lombok.Data;

@Data
@TableName("sys_config")
@SuppressWarnings("serial")
public class SysConfig extends DataEntity<Long> {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "code")
    private String code;

    @TableField(value = "setting")
    private String setting;

    @TableField(value = "value")
    private String value;

    @TableField(value = "is_sys")
    private Boolean isSys;

    @TableField(value = "type")
    private String type;

    @TableField(value = "tenant_id")
    private String tenantId;


}
