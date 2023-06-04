package com.sunseagear.wind.modules.test.twotable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sunseagear.common.mvc.entity.DataEntity;
import lombok.Data;

@Data
@TableName("test_car")
@SuppressWarnings("serial")
public class Car extends DataEntity<Long> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 汽车品牌
     */
    @TableField(value = "name")
    private String name;

    /**
     * 品牌编码
     */
    @TableField(value = "code")
    private String code;

}
