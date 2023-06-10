package com.sunseagear.wind.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.TreeEntity;
import lombok.Data;

import java.util.Date;

/**
 * @version V1.0
 * @Title:
 * @Description:
 * @date 2017-02-07 21:06:09
 */
@Data
@TableName("sys_organization")
@SuppressWarnings("serial")
public class Organization extends TreeEntity<Long> {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(exist = false)
    private String label;

    @TableField(value = "tenant_id")
    private String tenantId;

    @Override
    public void setName(String name) {
        super.setName(name);
        label = name;
    }

}
