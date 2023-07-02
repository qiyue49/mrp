package com.sunseagear.wind.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import lombok.Data;

@Data
@TableName("sys_dict")
@SuppressWarnings("serial")
public class Dict extends DataEntity<Long> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 分组ID
     */
    @TableField(value = "gid")
    private Long gid;
    /**
     * 键值名称
     */
    @TableField(value = "label")
    private String label;
    /**
     * 值
     */
    @TableField(value = "value")
    private String value;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;
    /**
     * 分组code
     */
    @TableField(exist = false)
    private String code;
}
