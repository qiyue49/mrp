package com.sunseagear.wind.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import lombok.Data;

@Data
@TableName("sys_dict_group")
@SuppressWarnings("serial")
public class DictGroup extends DataEntity<Long> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 分组名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 分组编码
     */
    @TableField(value = "code")
    private String code;

}
